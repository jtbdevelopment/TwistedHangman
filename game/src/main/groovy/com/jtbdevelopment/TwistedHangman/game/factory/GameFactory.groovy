package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:24 PM
 */
@Component
class GameFactory extends AbstractMultiPlayerGameFactory<Game, GameFeature> {
    @Override
    protected void copyFromPreviousGame(final Game previousGame, final Game newGame) {
        super.copyFromPreviousGame(previousGame, newGame)
        newGame.playerRunningScores.putAll(previousGame.playerRunningScores)
    }

    @Override
    protected Game newGame() {
        return new Game()
    }
}
