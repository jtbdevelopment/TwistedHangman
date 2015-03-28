package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.games.games.PlayerState
import com.jtbdevelopment.games.games.masked.MaskedMultiPlayerGame
import com.jtbdevelopment.games.rest.services.AbstractGameServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
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
class GameServices extends AbstractGameServices<ObjectId> {
    @Autowired
    StealLetterHandler stealLetterHandler
    @Autowired
    GuessLetterHandler guessLetterHandler
    @Autowired
    ChallengeToRematchHandler rematchHandler
    @Autowired
    ChallengeResponseHandler responseHandler
    @Autowired
    SetPuzzleHandler puzzleHandler
    @Autowired
    QuitHandler quitHandler

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rematch")
    MaskedMultiPlayerGame createRematch() {
        rematchHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get())
    }

    @PUT
    @Path("reject")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedMultiPlayerGame rejectGame() {
        responseHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), PlayerState.Rejected)
    }

    @PUT
    @Path("accept")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedMultiPlayerGame acceptGame() {
        responseHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), PlayerState.Accepted)
    }

    @PUT
    @Path("quit")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedMultiPlayerGame quitGame() {
        quitHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get())
    }

    @PUT
    @Path("puzzle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MaskedMultiPlayerGame setPuzzle(SetPuzzleHandler.CategoryAndWordPhrase categoryAndWordPhrase) {
        puzzleHandler.handleAction(
                (ObjectId) playerID.get(), (ObjectId) gameID.get(), categoryAndWordPhrase)
    }

    @PUT
    @Path("steal/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedMultiPlayerGame stealLetter(@PathParam("position") final int position) {
        stealLetterHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), position)
    }

    @PUT
    @Path("guess/{letter}")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedMultiPlayerGame guessLetter(@PathParam("letter") final String letter) {
        String validated = letter
        if (StringUtils.isEmpty(letter) || StringUtils.isEmpty(letter.trim())) {
            validated = " "
        }
        guessLetterHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), validated.charAt(0))
    }
}
