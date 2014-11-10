package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/4/2014
 * Time: 9:11 PM
 */
@CompileStatic
@Component
class ChallengeToRematchHandler extends AbstractNewGameHandler {
    public static final ZoneId GMT = ZoneId.of("GMT")

    public Game handleRematchRequest(
            final String initiatingPlayerID, final String previousGameID) {

        Game previousGame = loadGame(previousGameID)
        Player initiatingPlayer = loadPlayer(initiatingPlayerID)

        Game game = setupGame(previousGame, initiatingPlayer)

        previousGame.rematchId = game.id
        previousGame.rematched = ZonedDateTime.now(GMT)
        previousGame = transitionEngine.evaluateGamePhaseForGame(game)
        //  TODO notification of previous game

        //  TODO notifications
        game
    }

    private Game setupGame(final Game previousGame, final Player initiatingPlayer) {
        return transitionEngine.evaluateGamePhaseForGame(
                handleSystemPuzzleSetter(
                        gameRepository.save(
                                gameFactory.createGame(previousGame, initiatingPlayer))))
    }
}
