package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.games.rest.services.AbstractGameServices
import com.jtbdevelopment.games.state.PlayerState
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
    AbstractPlayerRotatingGameActionHandler guessLetterHandler
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
    Object createRematch() {
        rematchHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get())
    }

    @PUT
    @Path("reject")
    @Produces(MediaType.APPLICATION_JSON)
    Object rejectGame() {
        responseHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), PlayerState.Rejected)
    }

    @PUT
    @Path("accept")
    @Produces(MediaType.APPLICATION_JSON)
    Object acceptGame() {
        responseHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), PlayerState.Accepted)
    }

    @PUT
    @Path("quit")
    @Produces(MediaType.APPLICATION_JSON)
    Object quitGame() {
        quitHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get())
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
