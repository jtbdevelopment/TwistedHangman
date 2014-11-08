package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.exceptions.FailedToCreateValidGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/7/14
 * Time: 8:19 PM
 */
class GameFactoryTest extends GroovyTestCase {
    GameFactory gameFactory = new GameFactory();

    @Test
    public void testCreatingNewGame() {
        int validatorsCalled = 0
        int expandersCalled = 0
        int initializersCalled = 0
        int individualCalled = 0
        def initializer = [initializeGame: { initializersCalled++ }] as GameInitializer
        def validator = [
                validateGame: { validatorsCalled++; true },
                errorMessage: {
                    fail("Should not be called")
                }] as GameValidator
        def expander = [enhanceFeatureSet: { a, b -> expandersCalled++ }] as FeatureExpander
        def individual = [initializeIndividualGameStates: { individualCalled++ }] as IndividualGamesInitializer

        gameFactory.featureExpanders = [expander, expander, expander]
        gameFactory.gameValidators = [validator, validator]
        gameFactory.gameInitializers = [initializer, initializer, initializer, initializer]
        gameFactory.individualGamesInitializers = [individual, individual]


        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        String initiatingPlayer = "1"
        List<String> players = ["2", "3", "4"]
        def now = ZonedDateTime.now(ZoneId.of("GMT"))
        Thread.sleep(1)
        Game game = gameFactory.createGame(expectedFeatures, players, initiatingPlayer)

        assertNotNull game
        assert validatorsCalled == 2
        assert initializersCalled == 4
        assert expandersCalled == 3
        assert individualCalled == 2
        assert game.features == expectedFeatures
        assert game.players == ["2", "3", "4", "1"]
        assert game.initiatingPlayer == initiatingPlayer
        assert game.lastMove == game.created
        assert game.gamePhase == Game.GamePhase.Challenge
        assert game.wordPhraseSetter == ""
        assert game.created > now
        assertNull game.version
    }

    @Test
    public void testCreatingRematchGame() {
        int validatorsCalled = 0
        int expandersCalled = 0
        int initializersCalled = 0
        int individualCalled = 0
        def initializer = [initializeGame: { initializersCalled++ }] as GameInitializer
        def validator = [
                validateGame: { validatorsCalled++; true },
                errorMessage: {
                    fail("Should not be called")
                }] as GameValidator
        def expander = [enhanceFeatureSet: { a, b -> expandersCalled++ }] as FeatureExpander
        def individual = [initializeIndividualGameStates: { individualCalled++ }] as IndividualGamesInitializer

        gameFactory.featureExpanders = [expander, expander, expander]
        gameFactory.gameValidators = [validator, validator]
        gameFactory.gameInitializers = [initializer, initializer, initializer, initializer]
        gameFactory.individualGamesInitializers = [individual, individual]


        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        String initiatingPlayer = "1"
        List<String> players = ["2", "3", "4"]
        def now = ZonedDateTime.now(ZoneId.of("GMT"))
        Thread.sleep(1)

        Game priorGame = new Game();
        priorGame.features = expectedFeatures
        priorGame.players = players
        priorGame.players.add("1")
        priorGame.initiatingPlayer = "3"
        Game game = gameFactory.createGame(priorGame, initiatingPlayer)

        assertNotNull game
        assert validatorsCalled == 2
        assert initializersCalled == 4
        assert expandersCalled == 3
        assert individualCalled == 2
        assert game.features == expectedFeatures
        assert game.players == ["3", "4", "1", "2"]
        assert game.initiatingPlayer == initiatingPlayer
        assert game.lastMove == game.created
        assert game.gamePhase == Game.GamePhase.Challenge
        assert game.wordPhraseSetter == ""
        assert game.created > now
        assertNull game.version
    }

    @Test
    public void testErrorOnValidationFail() {
        int validatorsCalled = 0
        def validator = [
                validateGame: { validatorsCalled++; false },
                errorMessage: {
                    "TADA!"
                }] as GameValidator

        gameFactory.gameValidators = [validator, validator]


        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        String initiatingPlayer = "1"
        List<String> players = ["2", "3", "4"]

        Game priorGame = new Game();
        priorGame.features = expectedFeatures
        priorGame.players = players
        priorGame.players.add("1")
        priorGame.initiatingPlayer = "3"
        try {
            Game game = gameFactory.createGame(priorGame, initiatingPlayer)
            fail("Should have failed")
        } catch (FailedToCreateValidGameException e) {
            assert validatorsCalled == 2
            assert e.message == "System failed to create a valid game.  TADA!  TADA!  "
        }
    }
}
