package com.jtbdevelopment.TwistedHangman.game.factory.featureexpanders

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 11/3/14
 * Time: 9:29 PM
 */
class ThievingInitializerTest extends GroovyTestCase {
    private ThievingInitializer expander = new ThievingInitializer()


    public void testDoesNothingToNoneThievingOptions() {
        GameFeature.values().findAll() { GameFeature.Thieving != it }.each {
            def expected = [it] as Set
            Game game = new Game(features: expected)
            expander.initializeGame(game)
            assert expected == game.features
        }
    }


    public void testExpandsThievingOptions() {
        Set<GameFeature> expected = [GameFeature.ThievingPositionTracking, GameFeature.Thieving, GameFeature.ThievingCountTracking, GameFeature.ThievingLetters] as Set
        Game game = new Game(features: [GameFeature.Thieving] as Set)
        expander.initializeGame(game)
        assert expected == game.features
    }

    public void testOrder() {
        assert GameInitializer.DEFAULT_ORDER == expander.order
    }
}
