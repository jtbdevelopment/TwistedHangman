package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.state.masked.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:10 PM
 */
@Component
@CompileStatic
class NewGameHandler extends AbstractHandler {
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter
    @Autowired
    protected GameFactory gameFactory
    @Autowired
    protected GameRepository gameRepository
    @Autowired
    protected GamePhaseTransitionEngine transitionEngine
    @Autowired
    protected GameMasker gameMasker

    public MaskedGame handleCreateNewGame(
            final String initiatingPlayerID, final List<String> playersIDs, final Set<GameFeature> features) {
        Set<Player> players = loadPlayers(playersIDs)  //  Load as set to prevent dupes in initial setup
        Player initiatingPlayer = players.find { Player player -> player.id == initiatingPlayerID }
        if (initiatingPlayer == null) {
            initiatingPlayer = loadPlayer(initiatingPlayerID)
        }

        gameMasker.maskGameForPlayer(setupGame(features, players, initiatingPlayer), initiatingPlayer)
    }

    private Game setupGame(
            final Set<GameFeature> features,
            final Set<Player> players,
            final Player initiatingPlayer) {
        gameRepository.save(
                transitionEngine.evaluateGamePhaseForGame(
                        systemPuzzlerSetter.setWordPhraseFromSystem(
                                gameFactory.createGame(features, players.toList(), initiatingPlayer))))
    }

}
