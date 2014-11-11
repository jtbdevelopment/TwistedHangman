package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/4/2014
 * Time: 9:11 PM
 */
@CompileStatic
@Component
class ChallengeToRematchHandler extends AbstractGameActionHandler<Object> {
    //  TODO - revisit rematch later on
    public static final ZoneId GMT = ZoneId.of("GMT")
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter
    @Autowired
    protected GameFactory gameFactory

    @Override
    protected Game handleActionInternal(final Player player, final Game previousGame, final Object param = null) {
        Game newGame = setupGame(previousGame, player)

        previousGame.rematched = ZonedDateTime.now(GMT)
        Game transitioned = transitionEngine.evaluateGamePhaseForGame(previousGame)
        //  TODO notification of previous game

        //  TODO notifications
        newGame
    }

    private Game setupGame(final Game previousGame, final Player initiatingPlayer) {
        return systemPuzzlerSetter.setWordPhraseFromSystem(
                gameRepository.save(
                        gameFactory.createGame(previousGame, initiatingPlayer)))
    }
}
