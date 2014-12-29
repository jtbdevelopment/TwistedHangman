package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.gamecore.players.Player
import org.bson.types.ObjectId

/**
 * Date: 11/17/14
 * Time: 6:41 AM
 */
class GameGetterHandlerTest extends TwistedHangmanTestCase {
    GameGetterHandler handler = new GameGetterHandler()

    private final Game gameParam = new Game()
    private final ObjectId gameId = new ObjectId()

    public void testHandlerBasisc() {
        gameParam.players = [PTWO, PONE]
        gameParam.features.add(GameFeature.TurnBased)
        gameParam.featureData[GameFeature.TurnBased] = PONE.id
        gameParam.gamePhase = GamePhase.Playing

        handler.gameRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == gameId
                        return gameParam
                },
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        MaskedGame maskedGame = new MaskedGame()
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(gameParam)
                        assert p.is(PONE)
                        return maskedGame
                }
        ] as GameMasker

        assert maskedGame.is(handler.handleAction(PONE.id, gameId))
    }

}
