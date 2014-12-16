package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.friendfinder.FriendFinder
import groovy.transform.TypeChecked
import org.bson.types.ObjectId
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/15/2014
 * Time: 12:02 PM
 */
class PlayerServicesTest extends GroovyTestCase {
    PlayerServices playerServices = new PlayerServices()

    void testValidPlayer() {
        GameServices services = [playerID: new ThreadLocal<String>(), gameID: new ThreadLocal<String>()] as GameServices
        playerServices.gamePlayServices = services

        def APLAYER = new ObjectId()
        def AGAME = new ObjectId()
        playerServices.playerID.set(APLAYER)
        assert services.is(playerServices.gamePlay(AGAME.toHexString()))
        assert services.playerID.get() == APLAYER
        assert services.gameID.get() == AGAME
    }

    void testNullPlayer() {
        playerServices.gamePlayServices = null

        def APLAYER = new ObjectId()
        playerServices.playerID.set(APLAYER)

        OutboundJaxrsResponse resp = playerServices.gamePlay(null)
        assert resp.status == Response.Status.BAD_REQUEST.statusCode
        assert resp.entity == "Missing game identity"
    }

    void testEmptyPlayer() {
        playerServices.gamePlayServices = null

        def APLAYER = new ObjectId()
        playerServices.playerID.set(APLAYER)

        OutboundJaxrsResponse resp = playerServices.gamePlay("   ")
        assert resp.status == Response.Status.BAD_REQUEST.statusCode
        assert resp.entity == "Missing game identity"
    }


    void testGamePlayAnnotations() {
        def gamePlay = PlayerServices.getMethod("gamePlay", [String.class] as Class[])
        assert (gamePlay.annotations.size() == 1 ||
                (gamePlay.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gamePlay.annotations.size() == 2)
        )
        assert gamePlay.isAnnotationPresent(Path.class)
        assert gamePlay.getAnnotation(Path.class).value() == "game/{gameID}"
        def params = gamePlay.parameterAnnotations
        assert params.length == 1
        assert params[0].length == 1
        assert params[0][0].annotationType() == PathParam.class
        assert ((PathParam) params[0][0]).value() == "gameID"
    }

    void testCreateNewGame() {
        def APLAYER = new ObjectId()
        playerServices.playerID.set(APLAYER)
        def features = [GameFeature.AlternatingPuzzleSetter, GameFeature.DrawGallows] as Set
        def players = ["1", "2", "3"]
        PlayerServices.FeaturesAndPlayers input = new PlayerServices.FeaturesAndPlayers(features: features, players: players)
        MaskedGame game = new MaskedGame()
        playerServices.newGameHandler = [
                handleCreateNewGame: {
                    ObjectId i, List<String> p, Set<GameFeature> f ->
                        assert i == APLAYER
                        assert p == players
                        assert f == features
                        game
                }
        ] as NewGameHandler
        assert game.is(playerServices.createNewGame(input))
    }

    void testCreateNewGameAnnotations() {
        def gameServices = PlayerServices.getMethod("createNewGame", [PlayerServices.FeaturesAndPlayers.class] as Class[])
        assert (gameServices.annotations.size() == 4 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 5)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "new"
        assert gameServices.isAnnotationPresent(Consumes.class)
        assert gameServices.getAnnotation(Consumes.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(POST.class)
        def params = gameServices.parameterAnnotations
        assert params.length == 1
        assert params[0].length == 0
    }

    void testPlayerInfo() {
        Player p = new Player(id: new ObjectId(), displayName: "y", disabled: true, source: "here");
        playerServices.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == p.id
                        return p.clone()
                }
        ] as PlayerRepository

        playerServices.playerID.set(p.id)

        def returned = playerServices.playerInfo()
        assert p == returned
        assert returned.toString() == p.toString()
    }

    void testGetFriends() {
        def id = new ObjectId();
        playerServices.playerID.set(id)
        playerServices.friendFinder = [
                findFriends: {
                    ObjectId it ->
                        assert it == id
                        return ['1': '2', '3': '4', '5': '6']
                }
        ] as FriendFinder

        assert playerServices.getFriends() == ['1': '2', '3': '4', '5': '6']
    }
}
