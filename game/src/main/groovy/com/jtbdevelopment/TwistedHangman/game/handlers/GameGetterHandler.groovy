package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.games.masked.MaskedMultiPlayerGame
import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/17/14
 * Time: 6:37 AM
 */
@Component
@CompileStatic
class GameGetterHandler<ID extends Serializable> extends AbstractGameGetterHandler<ID> {
    MaskedMultiPlayerGame getGame(final ID playerID, final ID gameID) {
        Player player = loadPlayer(playerID)
        Game game = loadGame(gameID)
        validatePlayerForGame(game, player)
        return gameMasker.maskGameForPlayer(game, player)
    }
}
