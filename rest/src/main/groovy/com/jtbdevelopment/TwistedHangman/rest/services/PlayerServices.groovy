package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.PlayerRoles
import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import com.jtbdevelopment.gamecore.mongo.players.friendfinder.FriendFinder
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/14/14
 * Time: 6:40 AM
 */
@Component
@CompileStatic
class PlayerServices implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(PlayerServices.class)
    ThreadLocal<ObjectId> playerID = new ThreadLocal<>()

    @Autowired
    GameServices gamePlayServices
    @Autowired
    NewGameHandler newGameHandler
    @Autowired
    PlayerGamesFinderHandler playerGamesFinderHandler
    @Autowired
    AbstractPlayerRepository<ObjectId> playerRepository
    @Autowired
    AdminServices adminServices

    private ApplicationContext applicationContext;

    @Path("game/{gameID}")
    Object gamePlay(@PathParam("gameID") final String gameID) {
        if (StringUtils.isEmpty(gameID) || StringUtils.isEmpty(gameID.trim())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing game identity").build()
        }
        gamePlayServices.gameID.set(new ObjectId(gameID))
        gamePlayServices.playerID.set(playerID.get())
        return gamePlayServices
    }

    @Override
    void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext
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
    public Object playerInfo() {
        return playerRepository.findOne(playerID.get())
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("friends")
    public Map<String, Object> getFriends() {
        //  Social Media Requires Session Specific Requests
        if (applicationContext) {
            logger.info("Able to retrieve FriendFinder from application context");
            FriendFinder friendFinder = applicationContext.getBean(FriendFinder.class)
            return friendFinder.findFriends(playerID.get())
        } else {
            logger.warn("Unable to retrieve FriendFinder from application context");
            throw new IllegalStateException("No App Context")
        }
    }

    @Path("admin")
    @RolesAllowed([PlayerRoles.ADMIN])
    public Object adminServices() {
        adminServices.playerID.set(((SessionUserInfo<ObjectId>) SecurityContextHolder.context.authentication.principal).sessionUser.id)
        return adminServices
    }
}
