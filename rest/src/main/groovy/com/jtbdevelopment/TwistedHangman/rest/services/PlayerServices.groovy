package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.friendfinder.FriendFinder
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
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

    ThreadLocal<ObjectId> playerID = new ThreadLocal<>()

    @Autowired
    GameServices gamePlayServices
    @Autowired
    NewGameHandler newGameHandler
    @Autowired
    PlayerGamesFinderHandler playerGamesFinderHandler
    @Autowired
    PlayerRepository playerRepository
    @Autowired
    FriendFinder friendFinder

    @Path("game/{gameID}")
    Object gamePlay(@PathParam("gameID") final String gameID) {
        if (StringUtils.isEmpty(gameID) || StringUtils.isEmpty(gameID.trim())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing game identity").build()
        }
        gamePlayServices.gameID.set(new ObjectId(gameID))
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
    @Path("games")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MaskedGame> gamesForPlayer() {
        playerGamesFinderHandler.findGames(playerID.get())
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
