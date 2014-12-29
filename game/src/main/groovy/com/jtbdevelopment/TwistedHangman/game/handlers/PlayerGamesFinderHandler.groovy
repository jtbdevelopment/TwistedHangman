package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.gamecore.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/19/14
 * Time: 7:08 AM
 */
@Component
@CompileStatic
class PlayerGamesFinderHandler extends AbstractGameGetterHandler {
    private static int DEFAULT_PAGE_SIZE = 20;
    private static int DEFAULT_PAGE = 0;
    public static final ZoneId GMT = ZoneId.of("GMT")
    public static final Sort SORT = new Sort(Sort.Direction.DESC, ["lastUpdate", "created"])
    public static final PageRequest PAGE = new PageRequest(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, SORT)

    public List<MaskedGame> findGames(final ObjectId playerID) {
        Player player = loadPlayer(playerID);
        ZonedDateTime now = ZonedDateTime.now(GMT)

        List<MaskedGame> result = [];
        //  TODO - Would be nice to be parallel
        //  GPars and compile static not nice
        //  JDK1.8 streams and this build seemed to have issues
        //  shelving for now
        GamePhase.values().each {
            GamePhase phase ->
                def days = now.minusDays(phase.historyCutoffDays)
                result.addAll(gameRepository.findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan(
                        player.id,
                        phase,
                        days,
                        PAGE
                ).collect {
                    Game game ->
                        gameMasker.maskGameForPlayer(game, player)
                })
        }
        result
    }
}
