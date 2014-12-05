package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import org.springframework.data.domain.PageRequest

import java.time.ZonedDateTime

/**
 * Date: 12/4/2014
 * Time: 9:59 PM
 */
class PlayerGamesFinderHandlerTest extends TwistedHangmanTestCase {
    PlayerGamesFinderHandler handler = new PlayerGamesFinderHandler()

    void testTest() {
        def game1 = new Game(id: "1")
        def game2 = new Game(id: "2")
        def game3 = new Game(id: "3")
        def masked1 = new MaskedGame(id: "1")
        def masked2 = new MaskedGame(id: "2")
        def masked3 = new MaskedGame(id: "3")
        def queryResults = [
                (GamePhase.Challenged)      : [game1],
                (GamePhase.Declined)        : [],
                (GamePhase.NextRoundStarted): [game2],
                (GamePhase.Playing)         : [],
                (GamePhase.Quit)            : [],
                (GamePhase.RoundOver)       : [game3],
                (GamePhase.Setup)           : [],
        ]
        def maskResults = [
                (game1): masked1,
                (game2): masked2,
                (game3): masked3
        ]
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository

        handler.gameRepository = [
                findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan: {
                    String id, GamePhase gp, ZonedDateTime dt, PageRequest pr ->
                        return queryResults[gp]
                }
        ] as GameRepository
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game game, Player player ->
                        assert player.is(PONE)
                        return maskResults[game]
                }
        ] as GameMasker

        assert handler.findGames(PONE.id) as Set == [masked3, masked2, masked1] as Set
    }
}
