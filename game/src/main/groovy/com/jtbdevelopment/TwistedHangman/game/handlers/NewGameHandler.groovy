package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:10 PM
 */
@Component
@CompileStatic
class NewGameHandler extends AbstractHandler {
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter
    @Autowired
    protected GameFactory gameFactory
    @Autowired
    protected GameRepository gameRepository
    @Autowired
    protected GamePhaseTransitionEngine transitionEngine
    @Autowired
    protected GameMasker gameMasker
    @Autowired
    protected GamePublisher gamePublisher

    public MaskedGame handleCreateNewGame(
            final ObjectId initiatingPlayerID, final List<String> playersIDs, final Set<GameFeature> features) {
        Set<Player<ObjectId>> players = loadPlayerMD5s(playersIDs)  //  Load as set to prevent dupes in initial setup
        Player<ObjectId> initiatingPlayer = players.find { Player<ObjectId> player -> player.id == initiatingPlayerID }
        if (initiatingPlayer == null) {
            initiatingPlayer = loadPlayer(initiatingPlayerID)
        }


        def game = setupGame(features, players, initiatingPlayer)
        def playerGame = gameMasker.maskGameForPlayer(game, initiatingPlayer)
        playerGame
    }

    private Game setupGame(
            final Set<GameFeature> features,
            final Set<Player<ObjectId>> players,
            final Player<ObjectId> initiatingPlayer) {
        Game game = transitionEngine.evaluateGamePhaseForGame(
                systemPuzzlerSetter.setWordPhraseFromSystem(
                        gameFactory.createGame(features, players.toList(), initiatingPlayer)))
        gamePublisher.publish(gameRepository.save(game), initiatingPlayer)
    }

}
