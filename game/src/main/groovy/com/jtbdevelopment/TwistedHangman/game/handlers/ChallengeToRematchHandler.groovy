package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractChallengeToRematchHandler
import com.jtbdevelopment.games.state.MultiPlayerGame
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:11 PM
 */
@CompileStatic
@Component
class ChallengeToRematchHandler extends AbstractChallengeToRematchHandler {
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter

    @Override
    protected MultiPlayerGame setupGame(final MultiPlayerGame previousGame, final Player initiatingPlayer) {
        return systemPuzzlerSetter.setWordPhraseFromSystem(
                (Game) super.setupGame(previousGame, initiatingPlayer))
    }
}
