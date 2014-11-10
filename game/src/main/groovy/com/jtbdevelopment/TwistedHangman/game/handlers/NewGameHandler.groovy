package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:10 PM
 */
@Component
@CompileStatic
class NewGameHandler extends AbstractNewGameHandler {
    public Game handleCreateNewGame(
            final String initiatingPlayerID, final Set<String> playersIDs, final Set<GameFeature> features) {
        LinkedHashSet<Player> players = loadPlayers(playersIDs)
        Player initiatingPlayer = players.find { Player player -> player.id == initiatingPlayerID }
        if (initiatingPlayer == null) {
            initiatingPlayer = loadPlayer(initiatingPlayerID)
        }

        setupGame(features, players, initiatingPlayer)
    }

    private Game setupGame(
            final Set<GameFeature> features,
            final LinkedHashSet<Player> players,
            final Player initiatingPlayer) {
        transitionEngine.evaluateGamePhaseForGame(
                handleSystemPuzzleSetter(
                        gameRepository.save(
                                gameFactory.createGame(features, players, initiatingPlayer))))
    }

}
