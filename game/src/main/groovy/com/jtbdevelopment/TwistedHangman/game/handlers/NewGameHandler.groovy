package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:10 PM
 */
@Component
@CompileStatic
class NewGameHandler extends AbstractNewGameHandler {

    public Game handleCreateNewGame(
            final String initiatingPlayer, final List<String> players, final Set<GameFeature> features) {
        Game game = gameFactory.createGame(features, players, initiatingPlayer)
        game = gameRepository.save(game)
        handleSystemPuzzleSetter(game)

        //  TODO - state change and notification elsewhere
    }
}
