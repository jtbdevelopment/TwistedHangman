package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.games.events.GamePublisher
import com.jtbdevelopment.games.exceptions.input.OutOfGamesForTodayException
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractHandler
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame
import com.jtbdevelopment.games.state.masking.MultiPlayerGameMasker
import com.jtbdevelopment.games.tracking.GameEligibilityTracker
import com.jtbdevelopment.games.tracking.PlayerGameEligibility
import com.jtbdevelopment.games.tracking.PlayerGameEligibilityResult
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:10 PM
 */
@Component
@CompileStatic
class NewGameHandler extends AbstractHandler {
    private static final Logger logger = LoggerFactory.getLogger(NewGameHandler.class)

    @Autowired
    protected GameFactory gameFactory
    @Autowired
    protected GameRepository gameRepository
    @Autowired
    protected GamePhaseTransitionEngine transitionEngine
    @Autowired
    protected MultiPlayerGameMasker gameMasker
    @Autowired
    protected GamePublisher gamePublisher
    @Autowired
    protected GameEligibilityTracker gameTracker

    public MaskedMultiPlayerGame handleCreateNewGame(
            final ObjectId initiatingPlayerID,
            final List<String> playersIDs,
            final Set<GameFeature> features) {
        Set<Player<ObjectId>> players = loadPlayerMD5s(playersIDs)  //  Load as set to prevent dupes in initial setup
        Player<ObjectId> initiatingPlayer = players.find { Player<ObjectId> player -> player.id == initiatingPlayerID }
        if (initiatingPlayer == null) {
            initiatingPlayer = loadPlayer(initiatingPlayerID)
        }
        Game game = setupGameWithEligibilityWrapper(initiatingPlayer, features, players)

        def playerGame = gameMasker.maskGameForPlayer(
                (Game) gamePublisher.publish(game, initiatingPlayer),
                initiatingPlayer)
        playerGame
    }

    protected Game setupGameWithEligibilityWrapper(Player<ObjectId> initiatingPlayer, Set<GameFeature> features, Set<Player<ObjectId>> players) {
        PlayerGameEligibilityResult eligibilityResult = gameTracker.getGameEligibility(initiatingPlayer)
        if (eligibilityResult.eligibility == PlayerGameEligibility.NoGamesAvailable) {
            throw new OutOfGamesForTodayException()
        }

        Game game
        try {
            game = setupGame(features, players, initiatingPlayer)
        } catch (Exception e) {
            try {
                gameTracker.revertGameEligibility(eligibilityResult)
            } catch (Exception e2) {
                //  TODO - notification
                logger.warn('Failed to revert players game eligibility ' + eligibilityResult, e2)
            }
            throw e;
        }
        game
    }

    protected Game setupGame(
            final Set<GameFeature> features,
            final Set<Player<ObjectId>> players,
            final Player<ObjectId> initiatingPlayer) {
        Game game = transitionEngine.evaluateGame(
                gameFactory.createGame(features, players.toList(), initiatingPlayer))
        gameRepository.save(game)
    }

}
