package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotAvailableToRematchException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
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
class ChallengeToRematchHandler extends AbstractGameActionHandler<Object, Game> {
    public static final ZoneId GMT = ZoneId.of("GMT")
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter
    @Autowired
    protected GameFactory gameFactory

    @Override
    protected boolean requiresEligibilityCheck(final Object param) {
        return true;
    }

    @Override
    protected Game handleActionInternal(
            final Player player, final Game previousGame, final Object param) {
        if (previousGame.gamePhase != GamePhase.RoundOver) {
            throw new GameIsNotAvailableToRematchException()
        }
        previousGame.rematchTimestamp = ZonedDateTime.now(GMT)
        Game transitioned = (Game) gamePublisher.publish(
                (Game) gameRepository.save(
                        transitionEngine.evaluateGame(previousGame)),
                TwistedHangmanSystemPlayerCreator.TH_PLAYER)
        //  We set to system player so it gets published to all players, including this one
        //  TODO - handle newGame setup failing..
        Game newGame = setupGame(transitioned, player)
        newGame
    }

    private Game setupGame(final Game previousGame, final Player<ObjectId> initiatingPlayer) {
        return systemPuzzlerSetter.setWordPhraseFromSystem(
                gameFactory.createGame(previousGame, initiatingPlayer))
    }
}
