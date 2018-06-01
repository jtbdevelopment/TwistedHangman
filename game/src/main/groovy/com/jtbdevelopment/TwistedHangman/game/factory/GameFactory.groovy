package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory
import com.jtbdevelopment.games.factory.GameInitializer
import com.jtbdevelopment.games.factory.GameValidator
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:24 PM
 */
@Component
class GameFactory extends AbstractMultiPlayerGameFactory<ObjectId, GameFeature, Game> {
    protected GameFactory(
            final List<GameInitializer> gameInitializers,
            final List<GameValidator> gameValidators) {
        super(gameInitializers, gameValidators)
    }

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
