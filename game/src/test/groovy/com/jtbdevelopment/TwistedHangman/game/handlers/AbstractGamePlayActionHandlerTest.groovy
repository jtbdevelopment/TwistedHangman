package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerOutOfTurnException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.games.masked.MultiPlayerGameMasker
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import org.bson.types.ObjectId

/**
 * Date: 12/22/14
 * Time: 7:46 AM
 */
class AbstractGamePlayActionHandlerTest extends TwistedHangmanTestCase {
    class TestHandler extends AbstractGamePlayActionHandler<Object> {
        @Override
        protected Game handleActionInternal(final Player<ObjectId> player, final Game game, final Object param) {
            return game;
        }
    }
    private TestHandler handler = new TestHandler()
    private Game gameParam = new Game();
    private ObjectId gameId = new ObjectId()

    void testIgnoresNonTurnBasedGame() {
        Game saved = new Game();
        Game transitioned = new Game();
        Game published = new Game();
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(gameParam)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, null))
    }

    void testOKForCorrectPlayerTurn() {
        Game saved = new Game();
        Game transitioned = new Game();
        Game published = new Game();
        gameParam.players = [PONE, PTWO]
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        gameParam.features += GameFeature.TurnBased
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(gameParam)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, null))
    }

    void testFailsForOutOfPlayerTurn() {
        Game saved = new Game();
        Game transitioned = new Game();
        Game published = new Game();
        gameParam.players = [PONE, PTWO]
        gameParam.featureData[GameFeature.TurnBased] = PTWO.id
        gameParam.features += GameFeature.TurnBased
        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
                save   : {
                    Game it ->
                        assert it.is(transitioned)
                        return saved
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(gameParam)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        handler.gamePublisher = [
                publish: {
                    Game g, MongoPlayer p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        published
                }
        ] as GamePublisher
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, MongoPlayer p ->
                        assert g.is(published)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as MultiPlayerGameMasker

        try {
            handler.handleAction(PONE.id, gameId, null)
            fail("Should have failed")
        } catch (PlayerOutOfTurnException e) {
            //
        }
    }
}
