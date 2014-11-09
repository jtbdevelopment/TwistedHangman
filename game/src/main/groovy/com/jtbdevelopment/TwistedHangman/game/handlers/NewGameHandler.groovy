package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindPlayersException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:10 PM
 */
@Component
@CompileStatic
class NewGameHandler extends AbstractNewGameHandler {
    private static final Logger logger = LoggerFactory.getLogger(NewGameHandler.class)

    public Game handleCreateNewGame(
            final String initiatingPlayerID, final List<String> playersIDs, final Set<GameFeature> features) {

        List<Player> players = playerRepository.findAll(playersIDs).collect { Player it -> it }
        Player initiatingPlayer = playerRepository.findOne(initiatingPlayerID)

        validate(players, playersIDs, initiatingPlayer, initiatingPlayerID)

        setupGame(features, players, initiatingPlayer)
        //  TODO - notification
    }

    private Game setupGame(final Set<GameFeature> features, final List<Player> players, final Player initiatingPlayer) {
        transitionEngine.evaluateGamePhaseForGame(
                handleSystemPuzzleSetter(
                        gameRepository.save(
                                gameFactory.createGame(features, players, initiatingPlayer))))
    }

    private void validate(
            final List<Player> players,
            final List<String> playersIDs, final Player initiatingPlayer, String initiatingPlayerID) {
        if (players.size() != playersIDs.size()) {
            logger.info("Not all players were loaded " + playersIDs + " vs. " + players)
            throw new FailedToFindPlayersException()
        }
        if (initiatingPlayer == null) {
            logger.info("Initiating player was not loaded " + initiatingPlayerID)
            throw new FailedToFindPlayersException()
        }
    }
}
