package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.AbstractPlayerRotatingGameActionHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.StealLetterHandler
import com.jtbdevelopment.games.rest.AbstractMultiPlayerGameServices
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
class GameServices extends AbstractMultiPlayerGameServices<ObjectId> {
    @Autowired
    StealLetterHandler stealLetterHandler
    @Autowired
    AbstractPlayerRotatingGameActionHandler guessLetterHandler
    @Autowired
    SetPuzzleHandler puzzleHandler

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
