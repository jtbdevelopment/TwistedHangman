package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.games.exceptions.input.FailedToCreateValidGameException
import com.jtbdevelopment.games.factory.GameInitializer
import com.jtbdevelopment.games.factory.GameValidator
import com.jtbdevelopment.games.mongo.players.MongoPlayer

/**
 * Date: 11/7/14
 * Time: 8:19 PM
 */
class GameFactoryTest extends TwistedHangmanTestCase {
    GameFactory gameFactory = new GameFactory();

    public void testCreatingNewGame() {
        int validatorsCalled = 0
        int initializersCalled = 0
        boolean systemPhraseSetterCalled = false
        def initializer = [initializeGame: { initializersCalled++ }] as GameInitializer
        def validator = [
                validateGame: { validatorsCalled++; true },
                errorMessage: {
                    fail("Should not be called")
                }] as GameValidator

        gameFactory.gameValidators = [validator, validator]
        gameFactory.gameInitializers = [initializer, initializer, initializer, initializer]
        gameFactory.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    Game g ->
                        systemPhraseSetterCalled = true
                        g
                }
        ] as SystemPuzzlerSetter

        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        MongoPlayer initiatingPlayer = PONE
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        Thread.sleep(1)
        Game game = gameFactory.createGame(expectedFeatures, players, initiatingPlayer)

        assertNotNull game
        assert validatorsCalled == 2
        assert initializersCalled == 4
        assert systemPhraseSetterCalled
        assert game.features == expectedFeatures
        assert game.players == [PTWO, PTHREE, PFOUR, PONE]
        assert game.initiatingPlayer == initiatingPlayer.id
        assert game.lastUpdate == game.created
        assert game.wordPhraseSetter == null
        assert game.created == null
        assertNull game.version
    }


    public void testCreatingRematchGame() {
        int validatorsCalled = 0
        int initializersCalled = 0
        boolean systemPhraseSetterCalled = false
        def initializer = [initializeGame: { initializersCalled++ }] as GameInitializer
        def validator = [
                validateGame: { validatorsCalled++; true },
                errorMessage: {
                    fail("Should not be called")
                }] as GameValidator
        gameFactory.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    Game g ->
                        systemPhraseSetterCalled = true
                        g
                }
        ] as SystemPuzzlerSetter

        gameFactory.gameValidators = [validator, validator]
        gameFactory.gameInitializers = [initializer, initializer, initializer, initializer]


        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        MongoPlayer initiatingPlayer = PONE
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        Thread.sleep(1)

        Game priorGame = new Game();
        priorGame.features = expectedFeatures
        priorGame.players = players
        priorGame.players.add(PONE)
        priorGame.initiatingPlayer = PTHREE.id
        priorGame.round = new Random().nextInt(100)
        Game game = gameFactory.createGame(priorGame, initiatingPlayer)

        assertNotNull game
        assert validatorsCalled == 2
        assert initializersCalled == 4
        assert systemPhraseSetterCalled
        assert game.features == expectedFeatures
        assert game.players == [PTHREE, PFOUR, PONE, PTWO]
        assert game.initiatingPlayer == initiatingPlayer.id
        assert game.lastUpdate == game.created
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
        boolean systemPhraseSetterCalled = false
        gameFactory.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    Game g ->
                        systemPhraseSetterCalled = true
                        g
                }
        ] as SystemPuzzlerSetter


        Set<GameFeature> expectedFeatures = [GameFeature.DrawFace, GameFeature.SinglePlayer] as Set
        MongoPlayer initiatingPlayer = PONE
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]

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
            assert systemPhraseSetterCalled
        }
    }
}
