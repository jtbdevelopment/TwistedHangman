package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.exceptions.input.GameIsNotAvailableToRematchException
import com.jtbdevelopment.TwistedHangman.game.factory.GameFactory
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.utility.SystemPuzzlerSetter
import com.jtbdevelopment.gamecore.players.PlayerInt
import com.jtbdevelopment.gamecore.players.SystemPlayer
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
class ChallengeToRematchHandler extends AbstractGameActionHandler<Object> {
    public static final ZoneId GMT = ZoneId.of("GMT")
    @Autowired
    protected SystemPuzzlerSetter systemPuzzlerSetter
    @Autowired
    protected GameFactory gameFactory

    @Override
    protected Game handleActionInternal(final PlayerInt<ObjectId> player, final Game previousGame, final Object param) {
        if (previousGame.gamePhase != GamePhase.RoundOver) {
            throw new GameIsNotAvailableToRematchException()
        }
        previousGame.rematched = ZonedDateTime.now(GMT)
        Game transitioned = gamePublisher.publish(
                gameRepository.save(
                        transitionEngine.evaluateGamePhaseForGame(previousGame)),
                SystemPlayer.SYSTEM_PLAYER)
        //  We set to system player so it gets published to all players, including this one
        //  TODO - handle newGame setup failing..
        Game newGame = setupGame(transitioned, player)
        newGame
    }

    @Override
    protected Game rotateTurnBasedGame(final Game game) {
        //  Don't rotate, create already did
        return game
    }

    private Game setupGame(final Game previousGame, final PlayerInt<ObjectId> initiatingPlayer) {
        return systemPuzzlerSetter.setWordPhraseFromSystem(
                gameFactory.createGame(previousGame, initiatingPlayer))
    }
}
