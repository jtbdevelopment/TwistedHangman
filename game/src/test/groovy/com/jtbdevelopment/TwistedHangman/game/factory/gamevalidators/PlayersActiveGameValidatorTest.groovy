package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import org.junit.Test

/**
 * Date: 11/8/2014
 * Time: 1:11 PM
 */
class PlayersActiveGameValidatorTest extends TwistedHangmanTestCase {
    PlayersActiveGameValidator validator = new PlayersActiveGameValidator()

    @Test
    public void testPassesAllActives() {
        Game game = new Game()
        game.players = [PONE, PTWO]
        validator.playerRepository = [
                findAll: {
                    Iterable<String> input ->
                        assert input.collect { it } as Set == [PONE.id, PTWO.id] as Set
                        return [PONE, PTWO]
                },
        ] as PlayerRepository
        assert validator.validateGame(game)
    }

    @Test
    public void testFailsAnInactive() {
        Game game = new Game()
        game.players = [PONE, PINACTIVE2]
        validator.playerRepository = [
                findAll: {
                    Iterable<String> input ->
                        assert input.collect { it } as Set == [PONE.id, PINACTIVE2.id] as Set
                        return [PONE, PINACTIVE2]
                },
        ] as PlayerRepository
        assertFalse validator.validateGame(game)
    }

    @Test
    public void testErrorMessage() {
        assert validator.errorMessage() == "Game contains inactive players."
    }
}
