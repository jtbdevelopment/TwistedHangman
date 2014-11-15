package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/11/14
 * Time: 9:42 PM
 */
@Component
@CompileStatic
class GamePlayServices {
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    //  TODO - remove me
    String diagnostic() {
        return playerID.get() + " " + gameID.get()
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rematch")
    MaskedGame createRematch() {
        //  TODO send back cleaned up one
        rematchHandler.handleAction(playerID.get(), gameID.get())
    }

    @PUT
    @Path("reject")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame rejectGame() {
        responseHandler.handleAction(playerID.get(), gameID.get(), PlayerChallengeState.Rejected)
    }

    @PUT
    @Path("accept")
    @Produces(MediaType.APPLICATION_JSON)
    MaskedGame acceptGame() {
        responseHandler.handleAction(playerID.get(), gameID.get(), PlayerChallengeState.Accepted)
    }

    @PUT
    @Path("setpuzzle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    MaskedGame setPuzzle(@FormParam("category") final String category, @FormParam("wordPhrase") final String wordPhrase
    ) {
        puzzleHandler.handleAction(
                playerID.get(),
                gameID.get(),
                [(SetPuzzleHandler.CATEGORY_KEY): category, (SetPuzzleHandler.WORDPHRASE_KEY): wordPhrase])
    }

    @PUT
    @Path("steal/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    MaskedGame stealLetter(@PathParam("position") final int position) {
        stealLetterHandler.handleAction(playerID.get(), gameID.get(), position)
    }

    @PUT
    @Path("guess/{letter}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    MaskedGame guessLetter(@PathParam("letter") String letter) {
        if (letter.isEmpty()) {
            letter = " "
        }
        guessLetterHandler.handleAction(playerID.get(), gameID.get(), letter.charAt(0))
    }
}
