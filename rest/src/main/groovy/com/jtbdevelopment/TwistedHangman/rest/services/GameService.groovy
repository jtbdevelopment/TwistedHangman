package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/11/14
 * Time: 9:42 PM
 */
@Path("games")
@Component
@CompileStatic
class GameService {
    static final String PING_RESULT = "Alive."

    @Autowired
    StealLetterHandler stealLetterHandler
    @Autowired
    GuessLetterHandler guessLetterHandler
    @Autowired
    NewGameHandler newGameHandler
    @Autowired
    ChallengeToRematchHandler rematchHandler
    @Autowired
    ChallengeResponseHandler responseHandler
    @Autowired
    SetPuzzleHandler puzzleHandler

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("ping")
    String ping() {
        return PING_RESULT
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new/{playerID}")
    String createNewGame(
            @PathParam("playerID") final String playerID,
            @FormParam("players") final List<String> players,
            @FormParam("features") final Set<GameFeature> gameFeatures) {
        //  TODO send back cleaned up one
        Game game = newGameHandler.handleCreateNewGame(playerID, players, gameFeatures)
        return game.id
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rematch/{gameID}/{playerID}")
    String createRematch(
            @PathParam("playerID") final String playerID,
            @PathParam("gameID") final String gameID) {
        //  TODO send back cleaned up one
        Game game = rematchHandler.handleAction(playerID, gameID)
        return game.id
    }

    @PUT
    @Path("reject/{gameID}/{playerID}")
    @Produces(MediaType.APPLICATION_JSON)
    Game rejectGame(
            @PathParam("gameID") final String gameID,
            @PathParam("playerID") final String playerID) {
        Game game = responseHandler.handleAction(playerID, gameID, Game.PlayerChallengeState.Rejected)
        return game
    }

    @PUT
    @Path("accept/{gameID}/{playerID}")
    @Produces(MediaType.APPLICATION_JSON)
    Game acceptGame(
            @PathParam("gameID") final String gameID,
            @PathParam("playerID") final String playerID) {
        Game game = responseHandler.handleAction(playerID, gameID, Game.PlayerChallengeState.Accepted)
        return game
    }

    @PUT
    @Path("setpuzzle/{gameID}/{playerID}")
    @Produces(MediaType.APPLICATION_JSON)
    Game setPuzzle(
            @PathParam("gameID") final String gameID,
            @PathParam("playerID") final String playerID,
            @FormParam("category") final String category,
            @FormParam("wordPhrase") final String wordPhrase
    ) {
        Game game = puzzleHandler.handleAction(
                playerID,
                gameID,
                [(SetPuzzleHandler.CATEGORY_KEY): category, (SetPuzzleHandler.WORDPHRASE_KEY): wordPhrase])
        return game
    }

    @PUT
    @Path("steal/{gameID}/{playerID}/{position}")
    @Produces(MediaType.APPLICATION_JSON)
    Game stealLetter(
            @PathParam("gameID") final String gameID,
            @PathParam("playerID") final String playerID,
            @PathParam("position") final int position) {
        Game game = stealLetterHandler.handleAction(playerID, gameID, position)
        return game
    }

    @PUT
    @Path("guess/{gameID}/{playerID}/{letter}")
    @Produces(MediaType.APPLICATION_JSON)
    Game guessLetter(
            @PathParam("gameID") final String gameID,
            @PathParam("playerID") final String playerID,
            @PathParam("letter") String letter) {
        if (letter.isEmpty()) {
            letter = " "
        }
        Game game = guessLetterHandler.handleAction(playerID, gameID, letter.charAt(0))
        return game
    }
}
