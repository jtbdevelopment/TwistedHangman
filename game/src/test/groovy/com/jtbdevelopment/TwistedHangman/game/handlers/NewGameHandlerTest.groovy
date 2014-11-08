package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

/**
 * Date: 11/7/14
 * Time: 9:26 PM
 */
class NewGameHandlerTest extends THGroovyTestCase {
    NewGameHandler handler = new NewGameHandler()

    @Test
    public void testCreateNonSystemPuzzlerGame() {
        Set<GameFeature> features = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        List<Player> players = [PONE, PTWO, PTHREE]
        Player initiatingPlayer = PFOUR
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame = new Game()
        handler.gameFactory = [createGame: { a, b, c ->
            assert a == features
            assert b == players
            assert c == initiatingPlayer
            game
        }] as GameFactory
        handler.gameRepository = [
                save: {
                    assert it.is(game)
                    savedGame
                }
        ] as GameRepository
        handler.playerRepository = [
                findAll: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.id } as Set
                        return players
                },
                findOne: {
                    assert it == PFOUR.id
                    return PFOUR
                }
        ] as PlayerRepository

        assert savedGame == handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.id }, features)
    }


    @Test
    public void testSystemPuzzler() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<Player> players = [PTWO, PTHREE, PFOUR]
        Player initiatingPlayer = PONE
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame1 = new Game()
        savedGame1.features = features
        Game savedGame2 = new Game()
        handler.gameFactory = [createGame: { a, b, c ->
            assert a == features
            assert b == players
            assert c == initiatingPlayer
            game
        }] as GameFactory
        Game expectedSave = game
        handler.gameRepository = [
                save: {
                    assert it.is(expectedSave)
                    if (game.is(it)) {
                        expectedSave = savedGame1
                        return savedGame1
                    } else {
                        savedGame2
                    }
                }
        ] as GameRepository
        handler.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    assert it == savedGame1
                }
        ] as SystemPuzzlerSetter
        handler.playerRepository = [
                findAll: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.id } as Set
                        return players
                },
                findOne: {
                    assert it == PONE.id
                    return PONE
                }
        ] as PlayerRepository

        assert savedGame2 == handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.id }, features)
    }
}
