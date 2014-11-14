package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
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
    String createRematch() {
        //  TODO send back cleaned up one
        Game game = rematchHandler.handleAction(playerID.get(), gameID.get())
        return game.id
    }

    @PUT
    @Path("reject")
    @Produces(MediaType.APPLICATION_JSON)
    Game rejectGame() {
        Game game = responseHandler.handleAction(playerID.get(), gameID.get(), PlayerChallengeState.Rejected)
        return game
    }

    @PUT
    @Path("accept")
    @Produces(MediaType.APPLICATION_JSON)
    Game acceptGame() {
        Game game = responseHandler.handleAction(playerID.get(), gameID.get(), PlayerChallengeState.Accepted)
        return game
    }

    @PUT
    @Path("setpuzzle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Game setPuzzle(@FormParam("category") final String category, @FormParam("wordPhrase") final String wordPhrase
    ) {
        Game game = puzzleHandler.handleAction(
                playerID.get(),
                gameID.get(),
                [(SetPuzzleHandler.CATEGORY_KEY): category, (SetPuzzleHandler.WORDPHRASE_KEY): wordPhrase])
        return game
    }

    @PUT
    @Path("steal/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Game stealLetter(@PathParam("position") final int position) {
        Game game = stealLetterHandler.handleAction(playerID.get(), gameID.get(), position)
        return game
    }

    @PUT
    @Path("guess/{letter}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Game guessLetter(@PathParam("letter") String letter) {
        if (letter.isEmpty()) {
            letter = " "
        }
        Game game = guessLetterHandler.handleAction(playerID.get(), gameID.get(), letter.charAt(0))
        return game
    }
}
