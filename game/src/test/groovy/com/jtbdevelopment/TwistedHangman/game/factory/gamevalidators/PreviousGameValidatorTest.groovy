package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/5/2014
 * Time: 9:59 PM
 */
class PreviousGameValidatorTest extends GroovyTestCase {
    PreviousGameValidator validator = new PreviousGameValidator()

    @Test
    public void testErrorMessage() {
        assert "Prior game is not valid for rematch." == validator.errorMessage()
    }

    @Test
    public void testNoPreviousGameIsFine() {
        Game game = new Game()
        assert validator.validateGame(game)
    }

    @Test
    public void testPreviousGameIsInGoodState() {
        String id = "PR"
        Game previous = new Game()
        previous.gamePhase = Game.GamePhase.Rematch
        previous.id = id
        Game game = new Game()
        game.previousId = id

        validator.gameRepository = [findOne: { it -> assert it == id; return previous }] as GameRepository
        assert validator.validateGame(game)
    }

    @Test
    public void testFailsIfPreviousGameNullFromRepository() {
        String id = "PR"
        Game game = new Game()
        game.previousId = id

        validator.gameRepository = [findOne: { it -> assert it == id; return null }] as GameRepository
        assertFalse validator.validateGame(game)
    }

    @Test
    public void testPreviousGameHasNoIdFails() {
        String id = "PR"
        Game previous = new Game()
        previous.gamePhase = Game.GamePhase.Rematch
        previous.id = ""
        Game game = new Game()
        game.previousId = id

        validator.gameRepository = [findOne: { it -> assert it == id; return previous }] as GameRepository
        assert validator.validateGame(game)
    }

    @Test
    public void testPreviousGameIsInBadStates() {
        String id = "PR"
        Game previous = new Game()
        previous.gamePhase = Game.GamePhase.Rematch
        previous.id = id
        Game game = new Game()
        game.previousId = id

        validator.gameRepository = [findOne: { it -> assert it == id; return previous }] as GameRepository
        Game.GamePhase.values().findAll { it != Game.GamePhase.Rematch }.each {
            previous.gamePhase = it
            assertFalse validator.validateGame(game)
        }
    }
}
