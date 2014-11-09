package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.exceptions.FailedToFindPlayersException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:11 PM
 */
@CompileStatic
@Component
class ChallengeToRematchHandler extends AbstractNewGameHandler {

    public Game handleRematchRequest(
            final String initiatingPlayerID, final String previousGameID) {

        Game previousGame = gameRepository.findOne(previousGameID)
        Player initiatingPlayer = playerRepository.findOne(initiatingPlayerID)

        validate(previousGame, initiatingPlayer)

        Game game = setupGame(previousGame, initiatingPlayer)

        previousGame.rematchId = game.id
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

    private void validate(final Game previousGame, final Player initiatingPlayer) {
        if (previousGame == null) {
            throw new FailedToFindGameException()
        }
        if (initiatingPlayer == null) {
            throw new FailedToFindPlayersException()
        }
    }
}
