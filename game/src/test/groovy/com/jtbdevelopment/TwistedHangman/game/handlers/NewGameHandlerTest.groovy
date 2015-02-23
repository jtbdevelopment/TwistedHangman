package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.OutOfGamesForTodayException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.PlayerGameTracker
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.exceptions.system.FailedToFindPlayersException
import com.jtbdevelopment.games.games.masked.MultiPlayerGameMasker
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.publish.GamePublisher
import org.bson.types.ObjectId

/**
 * Date: 11/7/14
 * Time: 9:26 PM
 */
class NewGameHandlerTest extends TwistedHangmanTestCase {
    NewGameHandler handler = new NewGameHandler()


    public void testCreateGame() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        MongoPlayer initiatingPlayer = PONE
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
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    assert it.is(puzzled)
                    return transitionedGame
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(savedGame)
                        assert p.is(initiatingPlayer)
                        publishedGame
                }
        ] as GamePublisher
        handler.gameTracker = [
                getGameEligibility: {
                    Player<ObjectId> p ->
                        assert p.is(PONE)
                        return new PlayerGameTracker.GameEligibilityResult(eligibility: PlayerGameTracker.GameEligibility.FreeGameUsed, player: PONE)
                }
        ] as PlayerGameTracker
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(publishedGame)
                        assert p.is(initiatingPlayer)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features))
    }

    public void testCreateGameAndTransitionExceptions() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        MongoPlayer initiatingPlayer = PONE
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame = new Game()
        Game puzzled = new Game()
        savedGame.features = features
        boolean revertCalled = false
        handler.gameFactory = [createGame: { a, b, c ->
            assert a == features
            assert b == players
            assert c == initiatingPlayer
            game
        }] as GameFactory
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
                findOne    : {
                    assert it == PONE.id
                    return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    assert it.is(puzzled)
                    throw new IllegalArgumentException()
                }
        ] as GamePhaseTransitionEngine
        def eligibilityResult = new PlayerGameTracker.GameEligibilityResult(eligibility: PlayerGameTracker.GameEligibility.FreeGameUsed, player: PONE)
        handler.gameTracker = [
                getGameEligibility   : {
                    Player<ObjectId> p ->
                        assert p.is(PONE)
                        return eligibilityResult
                },
                revertGameEligibility: {
                    PlayerGameTracker.GameEligibilityResult r ->
                        assert r.is(eligibilityResult)
                        revertCalled = true
                }
        ] as PlayerGameTracker

        try {
            handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features)
            fail('exception expected')
        } catch (IllegalArgumentException e) {
            assert revertCalled
        }
    }

    public void testCreateGameAndRevertExceptionWrapped() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        MongoPlayer initiatingPlayer = PONE
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame = new Game()
        Game puzzled = new Game()
        savedGame.features = features
        boolean revertCalled = false
        handler.gameFactory = [createGame: { a, b, c ->
            assert a == features
            assert b == players
            assert c == initiatingPlayer
            game
        }] as GameFactory
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
                findOne    : {
                    assert it == PONE.id
                    return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    assert it.is(puzzled)
                    throw new IllegalArgumentException()
                }
        ] as GamePhaseTransitionEngine
        def eligibilityResult = new PlayerGameTracker.GameEligibilityResult(eligibility: PlayerGameTracker.GameEligibility.FreeGameUsed, player: PONE)
        handler.gameTracker = [
                getGameEligibility   : {
                    Player<ObjectId> p ->
                        assert p.is(PONE)
                        return eligibilityResult
                },
                revertGameEligibility: {
                    PlayerGameTracker.GameEligibilityResult r ->
                        assert r.is(eligibilityResult)
                        revertCalled = true
                        throw new IllegalStateException()
                }
        ] as PlayerGameTracker

        try {
            handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features)
            fail('exception expected')
        } catch (IllegalArgumentException e) {
            assert revertCalled
        } catch (IllegalStateException e) {
            fail('should have been wrapped')
        }
    }

    public void testCreateGameAndGameCreateExceptions() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        MongoPlayer initiatingPlayer = PONE
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame = new Game()
        boolean revertCalled = false
        savedGame.features = features
        handler.gameFactory = [createGame: { a, b, c ->
            assert a == features
            assert b == players
            assert c == initiatingPlayer
            throw new NumberFormatException()
        }] as GameFactory
        handler.playerRepository = [
                findByMd5In: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.md5 } as Set
                        return players
                },
                findOne    : {
                    assert it == PONE.id
                    return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        def eligibilityResult = new PlayerGameTracker.GameEligibilityResult(eligibility: PlayerGameTracker.GameEligibility.FreeGameUsed, player: PONE)
        handler.gameTracker = [
                getGameEligibility   : {
                    Player<ObjectId> p ->
                        assert p.is(PONE)
                        return eligibilityResult
                },
                revertGameEligibility: {
                    PlayerGameTracker.GameEligibilityResult r ->
                        assert r.is(eligibilityResult)
                        revertCalled = true
                }
        ] as PlayerGameTracker

        try {
            handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features)
            fail('exception expected')
        } catch (NumberFormatException e) {
            assert revertCalled
        }
    }

    public void testCreateGameFailsIfNotEligible() {
        Set<GameFeature> features = [GameFeature.SystemPuzzles, GameFeature.Thieving]
        List<MongoPlayer> players = [PTWO, PTHREE, PFOUR]
        MongoPlayer initiatingPlayer = PONE
        Game game = new Game()
        game.features.addAll(features)
        Game savedGame = new Game()
        Game puzzled = new Game()
        savedGame.features = features
        Game transitionedGame = new Game()
        Game publishedGame = new Game()
        handler.playerRepository = [
                findByMd5In: {
                    Iterable<String> it ->
                        assert it.collect { it } as Set == players.collect { it.md5 } as Set
                        return players
                },
                findOne    : {
                    assert it == PONE.id
                    return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.gameTracker = [
                getGameEligibility: {
                    Player<ObjectId> p ->
                        assert p.is(PONE)
                        return new PlayerGameTracker.GameEligibilityResult(eligibility: PlayerGameTracker.GameEligibility.NoGamesAvailable, player: PONE)
                }
        ] as PlayerGameTracker

        try {
            handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features)
            fail('should have failed')
        } catch (OutOfGamesForTodayException e) {
            //
        }
    }

    public void testInvalidInitiator() {
        Set<GameFeature> features = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        List<MongoPlayer> players = [PONE, PTWO, PTHREE]

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
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.handleCreateNewGame(playerId, players.collect { it.md5 }, features)
            fail("Should have failed")
        } catch (FailedToFindPlayersException e) {
            //
        }
    }


    public void testNotAllPlayersFound() {
        Set<GameFeature> features = [GameFeature.AlternatingPuzzleSetter, GameFeature.Thieving]
        List<MongoPlayer> players = [PONE, PTWO, PTHREE]
        MongoPlayer initiatingPlayer = PFOUR
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
        ] as AbstractPlayerRepository<ObjectId>

        try {
            handler.handleCreateNewGame(initiatingPlayer.id, players.collect { it.md5 }, features)
            fail("Should have failed")
        } catch (FailedToFindPlayersException e) {
            //
        }
    }
}
