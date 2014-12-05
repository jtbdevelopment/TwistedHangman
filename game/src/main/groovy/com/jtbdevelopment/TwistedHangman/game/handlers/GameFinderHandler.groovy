package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

/**
 * Date: 11/19/14
 * Time: 7:08 AM
 */
@CompileStatic
@Component
@Deprecated
class GameFinderHandler extends AbstractGameGetterHandler {

    public List<MaskedGame> findGames(final String playerID, final GamePhase gamePhase, int page, int pageSize) {
        Player player = loadPlayer(playerID);
        Sort sort = new Sort(Sort.Direction.DESC, ["lastUpdate", "created"])
        PageRequest pageRequest = new PageRequest(page, pageSize, sort)
        gameRepository.findByPlayersIdAndGamePhase(player.id, gamePhase, pageRequest).collect {
            Game game ->
                gameMasker.maskGameForPlayer(game, player)
        }
    }
}
