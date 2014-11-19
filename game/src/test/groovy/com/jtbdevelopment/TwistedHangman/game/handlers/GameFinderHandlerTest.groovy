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

/**
 * Date: 11/19/14
 * Time: 12:50 PM
 */
class GameFinderHandlerTest extends TwistedHangmanTestCase {
    GameFinderHandler handler = new GameFinderHandler()

    public void testHandlerBasic() {
        List<Game> games = [new Game(id: "1"), new Game(id: "2")]
        List<MaskedGame> maskedGames = [new MaskedGame(), new MaskedGame()]
        def seen = [] as Set
        def mgi = maskedGames.iterator()
        handler.gameRepository = [
                findByPlayersIdAndGamePhase: {
                    String it, GamePhase gp, PageRequest r ->
                        assert it == PONE.id
                        assert gp == GamePhase.Rematch
                        assert r.pageSize == 10
                        assert r.pageNumber == 2
                        assert r.sort != null
                        assert r.sort.toString() == "lastUpdate: DESC,created: DESC"
                        return games
                },
        ] as GameRepository
        handler.playerRepository = [
                findOne: {
                    String it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as PlayerRepository
        handler.gameMasker = [
                maskGameForPlayer: {
                    Game g, Player p ->
                        assert g.is(games[0]) || g.is(games[1])
                        seen.add(g.id)
                        return mgi.next()
                }
        ] as GameMasker

        List<MaskedGame> result = handler.findGames(PONE.id, GamePhase.Rematch, 2, 10)
        assert seen.size() == 2
        assert maskedGames[0].is(result[0])
        assert maskedGames[1].is(result[1])
        assert result.size() == 2
    }
}
