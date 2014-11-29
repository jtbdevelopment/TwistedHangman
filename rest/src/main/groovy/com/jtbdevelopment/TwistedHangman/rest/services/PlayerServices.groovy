package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.handlers.GameFinderHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.friendfinder.FriendFinder
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/14/14
 * Time: 6:40 AM
 */
@Component
@CompileStatic
class PlayerServices {
    public static int DEFAULT_PAGE_SIZE = 10
    public static int DEFAULT_PAGE = 0

    ThreadLocal<String> playerID = new ThreadLocal<>()

    @Autowired
    GamePlayServices gamePlayServices
    @Autowired
    NewGameHandler newGameHandler
    @Autowired
    GameFinderHandler gameFinderHandler
    @Autowired
    PlayerRepository playerRepository
    @Autowired
    FriendFinder friendFinder

    @Path("play/{gameID}")
    Object gamePlay(@PathParam("gameID") final String gameID) {
        if (StringUtils.isEmpty(gameID) || StringUtils.isEmpty(gameID.trim())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing game identity").build()
        }
        gamePlayServices.gameID.set(gameID)
        gamePlayServices.playerID.set(playerID.get())
        return gamePlayServices
    }

    static class FeaturesAndPlayers {
        List<String> players    // md5 list
        Set<GameFeature> features
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    MaskedGame createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
        newGameHandler.handleCreateNewGame(playerID.get(), featuresAndPlayers.players, featuresAndPlayers.features)
    }

    @GET
    @Path("games/{phase}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MaskedGame> gamesForPhase(
            @PathParam("phase") final GamePhase gamePhase,
            @QueryParam("page") final Integer page,
            @QueryParam("pageSize") final Integer pageSize) {
        gameFinderHandler.findGames(playerID.get(), gamePhase, page ?: DEFAULT_PAGE, pageSize ?: DEFAULT_PAGE_SIZE)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Player playerInfo() {
        return playerRepository.findOne(playerID.get())
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("friends")
    public Map<String, String> getFriends() {
        return friendFinder.findFriends(playerID.get())
    }
}
