package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Date: 11/4/14
 * Time: 6:55 PM
 */
@Component
@CompileStatic
class PreviousGameValidator implements GameValidator {
    public static final String ERROR = "Prior game is not valid for rematch."
    @Autowired
    GameRepository gameRepository

    @Override
    boolean validateGame(final Game game) {
        if (!StringUtils.isEmpty(game.previousId)) {
            Game priorGame = gameRepository.findOne(game.previousId)
            if (priorGame == null || priorGame.gamePhase != Game.GamePhase.Rematch || !StringUtils.isEmpty(priorGame.rematchId)) {
                return false
            }
        }
        return true
    }

    @Override
    String errorMessage() {
        return ERROR
    }
}
