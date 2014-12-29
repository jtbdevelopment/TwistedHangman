package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.publish.GamePublisher
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/9/2014
 * Time: 8:36 PM
 */
@CompileStatic
abstract class AbstractGameActionHandler<T> extends AbstractGameGetterHandler {
    @Autowired
    protected GamePhaseTransitionEngine transitionEngine
    @Autowired
    protected GamePublisher gamePublisher

    abstract protected Game handleActionInternal(final Player player, final Game game, T param);

    public MaskedGame handleAction(final ObjectId playerID, final ObjectId gameID, T param = null) {
        Player player = loadPlayer(playerID)
        Game game = loadGame(gameID)
        validatePlayerForGame(game, player)
        return gameMasker.maskGameForPlayer(
                gamePublisher.publish(
                        gameRepository.save(
                                transitionEngine.evaluateGamePhaseForGame(
                                        rotateTurnBasedGame(
                                                handleActionInternal(player, game, param)))),
                        player),
                player)
    }

    protected Game rotateTurnBasedGame(final Game game) {
        if (game.gamePhase == GamePhase.Playing && game.features.contains(GameFeature.TurnBased)) {
            rotateOnePlayer(game)

            if (game.featureData[GameFeature.TurnBased] == game.wordPhraseSetter) {
                rotateOnePlayer(game);
            }
        }
        game
    }

    protected static void rotateOnePlayer(final Game game) {
        int index = game.players.findIndexOf { Player p -> p.id == game.featureData[GameFeature.TurnBased] } + 1
        if (index >= game.players.size()) {
            index = 0
        }
        game.featureData[GameFeature.TurnBased] = game.players[index].id
    }

}
