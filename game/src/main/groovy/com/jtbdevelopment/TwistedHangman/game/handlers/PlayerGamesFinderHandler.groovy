package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collectors

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

    public List<MaskedGame> findGames(final String playerID) {
        Player player = loadPlayer(playerID);
        ZonedDateTime now = ZonedDateTime.now(GMT)

        return GamePhase.values().toList().parallelStream().map(new Function<GamePhase, List<Game>>() {
            @Override
            List<Game> apply(final GamePhase phase) {
                def days = now.minusDays(phase.historyCutoffDays)
                return gameRepository.findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan(
                        player.id,
                        phase,
                        days,
                        PAGE
                )
            }
        }).map(new Function<List<Game>, List<MaskedGame>>() {

            @Override
            List<MaskedGame> apply(final List<Game> games) {
                games.parallelStream().map(new Function<Game, MaskedGame>() {
                    @Override
                    MaskedGame apply(final Game game) {
                        return gameMasker.maskGameForPlayer(game, player)
                    }
                }).collect(Collectors.toList())
            }
        }).reduce(new BinaryOperator<List<MaskedGame>>() {
            @Override
            List<Game> apply(final List<MaskedGame> games, final List<MaskedGame> games2) {
                games.addAll(games2)
                games
            }
        }).get()
    }
}
