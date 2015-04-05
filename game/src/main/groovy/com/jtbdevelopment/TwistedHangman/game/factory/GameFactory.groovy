package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory
import com.jtbdevelopment.games.players.Player
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/3/14
 * Time: 9:24 PM
 */
@Component
class GameFactory extends AbstractMultiPlayerGameFactory<Game, GameFeature> {
    @Autowired
    List<FeatureExpander> featureExpanders = []
    @Autowired
    List<IndividualGamesInitializer> individualGamesInitializers = []

    @Override
    protected void copyFromPreviousGame(final Game previousGame, final Game newGame) {
        newGame.previousId = previousGame.id
        newGame.round = previousGame.round + 1
        newGame.playerRunningScores.putAll(previousGame.playerRunningScores)
    }

    @Override
    protected void initializeGame(final Game game) {
        featureExpanders.each {
            FeatureExpander it ->
                it.enhanceFeatureSet(game.features, game.players)
        }
        super.initializeGame(game)
        individualGamesInitializers.each {
            IndividualGamesInitializer it ->
                it.initializeIndividualGameStates(game)
        }
    }

    @Override
    protected Game newGame() {
        return new Game()
    }

    @Override
    protected Game createFreshGame(
            final Set<GameFeature> features, final List<Player> players, final Player initiatingPlayer) {
        Game game = (Game) super.createFreshGame(features, players, initiatingPlayer)
        game.gamePhase = GamePhase.Challenged
        game.wordPhraseSetter = null
        game.round = 1;
        game
    }
}
