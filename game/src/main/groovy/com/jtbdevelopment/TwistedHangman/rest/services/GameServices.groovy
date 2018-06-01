package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.AbstractPlayerRotatingGameActionHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.GuessLetterHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.StealLetterHandler
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.rest.AbstractMultiPlayerGameServices
import com.jtbdevelopment.games.rest.handlers.*
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/11/14
 * Time: 9:42 PM
 */
@Component
@CompileStatic
class GameServices extends AbstractMultiPlayerGameServices<
        ObjectId,
        GameFeature,
        Game,
        MaskedGame,
        MongoPlayer> {

    private final StealLetterHandler stealLetterHandler
    private final AbstractPlayerRotatingGameActionHandler guessLetterHandler
    private final SetPuzzleHandler puzzleHandler

    GameServices(
            final GameGetterHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> gameGetterHandler,
            final DeclineRematchOptionHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> declineRematchOptionHandler,
            final ChallengeResponseHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> responseHandler,
            final ChallengeToRematchHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> rematchHandler,
            final QuitHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> quitHandler,
            final StealLetterHandler stealLetterHandler,
            final GuessLetterHandler guessLetterHandler,
            final SetPuzzleHandler setPuzzleHandler) {
        super(gameGetterHandler, declineRematchOptionHandler, responseHandler, rematchHandler, quitHandler)
        this.stealLetterHandler = stealLetterHandler
        this.guessLetterHandler = guessLetterHandler
        this.puzzleHandler = setPuzzleHandler
    }

    @PUT
    @Path("puzzle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Object setPuzzle(SetPuzzleHandler.CategoryAndWordPhrase categoryAndWordPhrase) {
        puzzleHandler.handleAction(
                (ObjectId) playerID.get(), (ObjectId) gameID.get(), categoryAndWordPhrase)
    }

    @PUT
    @Path("steal/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    Object stealLetter(@PathParam("position") final int position) {
        stealLetterHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), position)
    }

    @PUT
    @Path("guess/{letter}")
    @Produces(MediaType.APPLICATION_JSON)
    Object guessLetter(@PathParam("letter") final String letter) {
        String validated = letter
        if (StringUtils.isEmpty(letter) || StringUtils.isEmpty(letter.trim())) {
            validated = " "
        }
        guessLetterHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), validated.charAt(0))
    }
}
