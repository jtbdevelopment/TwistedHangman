package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import org.bson.types.ObjectId

/**
 * Date: 11/8/2014
 * Time: 1:11 PM
 */
class PlayersActiveGameValidatorTest extends TwistedHangmanTestCase {
    PlayersActiveGameValidator validator = new PlayersActiveGameValidator()


    public void testPassesAllActives() {
        Game game = new Game()
        game.players = [PONE, PTWO]
        validator.playerRepository = [
                findAll: {
                    Iterable<String> input ->
                        assert input.collect { it } as Set == [PONE.id, PTWO.id] as Set
                        return [PONE, PTWO]
                },
        ] as AbstractPlayerRepository<ObjectId>
        assert validator.validateGame(game)
    }


    public void testFailsAnInactive() {
        Game game = new Game()
        game.players = [PONE, PINACTIVE2]
        validator.playerRepository = [
                findAll: {
                    Iterable<String> input ->
                        assert input.collect { it } as Set == [PONE.id, PINACTIVE2.id] as Set
                        return [PONE, PINACTIVE2]
                },
        ] as AbstractPlayerRepository<ObjectId>
        assertFalse validator.validateGame(game)
    }


    public void testErrorMessage() {
        assert validator.errorMessage() == "Game contains inactive players."
    }
}
