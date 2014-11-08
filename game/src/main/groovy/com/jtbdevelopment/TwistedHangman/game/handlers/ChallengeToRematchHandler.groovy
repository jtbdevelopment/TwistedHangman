package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

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

        Game game = gameFactory.createGame(previousGame, initiatingPlayer)
        Game saved = gameRepository.save(game)
        if (!StringUtils.isEmpty(saved.previousId)) {
            Game previous = gameRepository.findOne(saved.previousId)
            previous.gamePhase = Game.GamePhase.Rematched
            previous.rematchId = saved.id
            gameRepository.save(previous)
        }
        game = handleSystemPuzzleSetter(game)

        //  TODO change state and notifications
        game
    }
}
