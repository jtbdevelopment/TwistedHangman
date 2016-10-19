package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory
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
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter

    @Override
    protected void copyFromPreviousGame(final Game previousGame, final Game newGame) {
        super.copyFromPreviousGame(previousGame, newGame)
        newGame.playerRunningScores.putAll(previousGame.playerRunningScores)
    }

    @Override
    protected void initializeGame(final Game game) {
        //  TODO - convert feature exapnders/individual game initializers to standard initializers with early/late ordering
        featureExpanders.each {
            FeatureExpander it ->
                it.enhanceFeatureSet(game.features, game.players)
        }
        super.initializeGame(game)
        individualGamesInitializers.each {
            IndividualGamesInitializer it ->
                it.initializeIndividualGameStates(game)
        }
        systemPuzzlerSetter.setWordPhraseFromSystem(game)
    }

    @Override
    protected Game newGame() {
        return new Game()
    }
}
