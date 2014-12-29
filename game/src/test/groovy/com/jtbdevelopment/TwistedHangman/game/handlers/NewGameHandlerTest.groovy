package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher
import com.jtbdevelopment.gamecore.players.Player
import org.bson.types.ObjectId

/**
 * Date: 11/7/14
 * Time: 9:26 PM
 */
class NewGameHandlerTest extends TwistedHangmanTestCase {
    NewGameHandler handler = new NewGameHandler()


    public void testCreateGame() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<Player> players = [PTWO, PTHREE, PFOUR]
        Player initiatingPlayer = PONE
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame = new Game()
        Game puzzled = new Game()
        savedGame.features = features
        Game transitionedGame = new Game()
        Game publishedGame = new Game()
        handler.gameFactory = [createGame: { a, b, c ->
            assert a == features
            assert b == players
            assert c == initiatingPlayer
            game
        }] as GameFactory
        handler.gameRepository = [
                save: {
                    assert it.is(transitionedGame)
                    return savedGame
                }
        ] as GameRepository
        handler.systemPuzzlerSetter = [
                setWordPhraseFromSystem: {
                    assert it.is(game)
                    return puzzled
                }
        ] as SystemPuzzlerSetter
        handler.playerRepository = [
                findByMd5In: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.md5 } as Set
                        return players
                },
                findOne: {
                    assert it == PONE.id
                    return PONE
                }
        ] as PlayerRepository
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    assert it.is(puzzled)
                    return transitionedGame
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, Player p ->
                        assert g.is(savedGame)
                        assert p.is(initiatingPlayer)
                        publishedGame
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(publishedGame)
                        assert p.is(initiatingPlayer)
                        return maskedGame
                }
        ] as GameMasker

        assert maskedGame.is(handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features))
    }


    public void testInvalidInitiator() {
        Set<GameFeature> features = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        List<Player> players = [PONE, PTWO, PTHREE]

        ObjectId playerId = new ObjectId();
        handler.playerRepository = [
                findByMd5In: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.md5 } as Set
                        return players
                },
                findOne: {
                    assert it == playerId
                    return null
                }
        ] as PlayerRepository

        try {
            handler.handleCreateNewGame(playerId, players.collect { it.md5 }, features)
            fail("Should have failed")
        } catch (FailedToFindPlayersException e) {
            //
        }
    }


    public void testNotAllPlayersFound() {
        Set<GameFeature> features = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        List<Player> players = [PONE, PTWO, PTHREE]
        Player initiatingPlayer = PFOUR
        handler.playerRepository = [
                findByMd5In: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.md5 } as Set
                        return [PONE, PTHREE]
                },
                findOne: {
                    assert it == PFOUR.id
                    return PFOUR
                }
        ] as PlayerRepository

        try {
            handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features)
            fail("Should have failed")
        } catch (FailedToFindPlayersException e) {
            //
        }
    }
}
