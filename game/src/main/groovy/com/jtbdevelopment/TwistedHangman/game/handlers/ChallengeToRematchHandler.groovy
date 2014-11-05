package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
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
    public handleRematchRequest(
            final String initiatingPlayer, final Game previousGame) {

        Game game = gameFactory.createGame(previousGame, initiatingPlayer)
        Game saved = gameRepository.save(game)
        if (!StringUtils.isEmpty(saved.previousId)) {
            Game previous = gameRepository.findOne(saved.previousId)
            previous.gamePhase = Game.GamePhase.Rematched
            previous.rematchId = saved.id
            gameRepository.save(previous)
        }
        handleSystemPuzzleSetter(game)

        //  TODO change state and notifications
    }
}
