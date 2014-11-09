package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhaseTransitionEngine
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

/**
 * Date: 11/4/2014
 * Time: 9:54 PM
 */
@CompileStatic
abstract class AbstractNewGameHandler {
    @Autowired
    protected GameRepository gameRepository
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter
    @Autowired
    protected GameFactory gameFactory
    @Autowired
    protected PlayerRepository playerRepository
    @Autowired
    protected GamePhaseTransitionEngine transitionEngine


    protected Game handleSystemPuzzleSetter(Game game) {
        if (game.features.contains(GameFeature.SystemPuzzles)) {
            systemPuzzlerSetter.setWordPhraseFromSystem(game)
            return gameRepository.save(game)
        }
        game
    }
}
