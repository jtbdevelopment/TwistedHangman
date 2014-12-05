package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.TwistedHangman.game.state.PlayerState
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import groovy.transform.CompileStatic
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
class GameServices {
    ThreadLocal<String> playerID = new ThreadLocal<>()
    ThreadLocal<String> gameID = new ThreadLocal<>()

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
    GameGetterHandler gameGetterHandler
    @Autowired
    QuitHandler quitHandler

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame getGame() {
        gameGetterHandler.handleAction(playerID.get(), gameID.get())
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rematch")
    MaskedGame createRematch() {
        //  TODO - return both?
        rematchHandler.handleAction(playerID.get(), gameID.get())
    }

    @PUT
    @Path("reject")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame rejectGame() {
        responseHandler.handleAction(playerID.get(), gameID.get(), PlayerState.Rejected)
    }

    @PUT
    @Path("accept")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame acceptGame() {
        responseHandler.handleAction(playerID.get(), gameID.get(), PlayerState.Accepted)
    }

    @PUT
    @Path("quit")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame quitGame() {
        quitHandler.handleAction(playerID.get(), gameID.get())
    }

    @PUT
    @Path("puzzle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MaskedGame setPuzzle(SetPuzzleHandler.CategoryAndWordPhrase categoryAndWordPhrase) {
        puzzleHandler.handleAction(
                playerID.get(), gameID.get(), categoryAndWordPhrase)
    }

    @PUT
    @Path("steal/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame stealLetter(@PathParam("position") final int position) {
        stealLetterHandler.handleAction(playerID.get(), gameID.get(), position)
    }

    @PUT
    @Path("guess/{letter}")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame guessLetter(@PathParam("letter") final String letter) {
        String validated = letter
        if (StringUtils.isEmpty(letter) || StringUtils.isEmpty(letter.trim())) {
            validated = " "
        }
        guessLetterHandler.handleAction(playerID.get(), gameID.get(), validated.charAt(0))
    }
}
