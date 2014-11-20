package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.exceptions.input.FailedToCreateValidGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/7/14
 * Time: 8:19 PM
 */
class GameFactoryTest extends TwistedHangmanTestCase {
    GameFactory gameFactory = new GameFactory();


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
        Player initiatingPlayer = PONE
        List<Player> players = [PTWO, PTHREE, PFOUR]
        Thread.sleep(1)
        Game game = gameFactory.createGame(expectedFeatures, players, initiatingPlayer)

        assertNotNull game
        assert validatorsCalled == 2
        assert initializersCalled == 4
        assert expandersCalled == 3
        assert individualCalled == 2
        assert game.features == expectedFeatures
        assert game.players == [PTWO, PTHREE, PFOUR, PONE]
        assert game.initiatingPlayer == initiatingPlayer.id
        assert game.lastUpdate == game.created
        assert game.gamePhase == GamePhase.Challenge
        assert game.wordPhraseSetter == null
        assert game.created == null
        assertNull game.version
    }


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
        Player initiatingPlayer = PONE
        List<Player> players = [PTWO, PTHREE, PFOUR]
        Thread.sleep(1)

        Game priorGame = new Game();
        priorGame.features = expectedFeatures
        priorGame.players = players
        priorGame.players.add(PONE)
        priorGame.initiatingPlayer = PTHREE.id
        Game game = gameFactory.createGame(priorGame, initiatingPlayer)

        assertNotNull game
        assert validatorsCalled == 2
        assert initializersCalled == 4
        assert expandersCalled == 3
        assert individualCalled == 2
        assert game.features == expectedFeatures
        assert game.players == [PTHREE, PFOUR, PONE, PTWO]
        assert game.initiatingPlayer == initiatingPlayer.id
        assert game.lastUpdate == game.created
        assert game.gamePhase == GamePhase.Challenge
        assert game.wordPhraseSetter == null
        assert game.created == null
        assertNull game.version
    }


    public void testErrorOnValidationFail() {
        int validatorsCalled = 0
        def validator = [
                validateGame: { validatorsCalled++; false },
                errorMessage: {
                    "TADA!"
                }] as GameValidator

        gameFactory.gameValidators = [validator, validator]


        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        Player initiatingPlayer = PONE
        List<Player> players = [PTWO, PTHREE, PFOUR]

        Game priorGame = new Game();
        priorGame.features = expectedFeatures
        priorGame.players = players
        priorGame.players.add(PONE)
        priorGame.initiatingPlayer = PTHREE.id
        try {
            gameFactory.createGame(priorGame, initiatingPlayer)
            fail("Should have failed")
        } catch (FailedToCreateValidGameException e) {
            assert validatorsCalled == 2
            assert e.message == "System failed to create a valid game.  TADA!  TADA!  "
        }
    }
}