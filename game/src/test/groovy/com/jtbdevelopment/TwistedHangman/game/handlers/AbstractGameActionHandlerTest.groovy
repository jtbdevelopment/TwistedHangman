package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.exceptions.input.PlayerNotPartOfGameException
import com.jtbdevelopment.TwistedHangman.exceptions.system.FailedToFindGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 11/10/14
 * Time: 7:06 PM
 */
class AbstractGameActionHandlerTest extends TwistedHangmanTestCase {
    private static final String testParam = "TESTPARAM"
    private static final Game handledGame = new Game();
    private final Game gameParam = new Game()
    private final String gameId = "GAMEID"

    private class TestHandler extends AbstractGameActionHandler<String> {
        @Override
        protected Game handleActionInternal(final Player player, final Game game, final String param) {
            assert param == testParam
            assert gameParam.is(game)
            return handledGame
        }
    }
    private TestHandler handler = new TestHandler()


    public void testAbstractHandlerBase() {
        Game saved = new Game();
        Game transitioned = new Game();
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    String it ->
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
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(handledGame)
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as GameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }


    public void testAbstractHandlerCantLoadGame() {
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    String it ->
                        assert it == gameId
                        return null
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository

        try {
            handler.handleAction(PONE.id, gameId, testParam)
            fail("should have failed")
        } catch (FailedToFindGameException e) {

        }
    }


    public void testAbstractHandlerInvalidPlayer() {
        gameParam.players = [PONE, PTWO]
        handler.gameRepository = [
                findOne: {
                    String it ->
                        assert it == gameId
                        return gameParam
                }
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PTHREE.id
                        return PTHREE
                }
        ] as PlayerRepository

        try {
            handler.handleAction(PTHREE.id, gameId, testParam)
            fail("should have failed")
        } catch (PlayerNotPartOfGameException e) {

        }
    }


    public void testAbstractHandlerBaseRotatesTurn() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.add(GameFeature.TurnBased)
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Playing
        gameParam.gamePhase = GamePhase.Playing

        Game saved = gameParam.clone()
        Game transitioned = gameParam.clone();
        handler.gameRepository = [
                findOne: {
                    String it ->
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
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PTWO.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as GameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }


    public void testAbstractHandlerBaseDoesNotRotateTurnWhenNotPlaying() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.add(GameFeature.TurnBased)
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Setup
        gameParam.gamePhase = GamePhase.Setup

        Game saved = gameParam.clone()
        Game transitioned = gameParam.clone();
        handler.gameRepository = [
                findOne: {
                    String it ->
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
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PONE.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as GameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }


    public void testAbstractHandlerBaseDoesNotRotateTurnWhenPlayingNonTurnedBasedGame() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.remove(GameFeature.TurnBased)
        gameParam.featureData = [(GameFeature.TurnBased): PONE.id]
        handledGame.featureData.clear()
        handledGame.featureData.putAll(gameParam.featureData)
        handledGame.features.clear()
        handledGame.features.addAll(gameParam.features)
        handledGame.players.clone()
        handledGame.players.addAll(gameParam.players)
        handledGame.gamePhase = GamePhase.Playing
        gameParam.gamePhase = GamePhase.Playing

        Game saved = gameParam.clone()
        Game transitioned = gameParam.clone();
        handler.gameRepository = [
                findOne: {
                    String it ->
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
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        handler.transitionEngine = [
                evaluateGamePhaseForGame: {
                    Game it ->
                        assert it.is(handledGame)
                        assert handledGame.featureData[GameFeature.TurnBased] == PONE.id
                        return transitioned
                }
        ] as GamePhaseTransitionEngine
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(saved)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as GameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId, testParam))
    }
}
