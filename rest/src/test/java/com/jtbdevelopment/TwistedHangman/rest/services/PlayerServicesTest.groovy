package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.handlers.GameFinderHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.TwistedHangman.players.friendfinder.FriendFinder
import groovy.transform.TypeChecked
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/15/2014
 * Time: 12:02 PM
 */
class PlayerServicesTest extends GroovyTestCase {
    PlayerServices playerServices = new PlayerServices()

    void testValidPlayer() {
        GameServices services = [playerID: new ThreadLocal<String>(), gameID: new ThreadLocal<String>()] as GameServices
        playerServices.gamePlayServices = services

        def APLAYER = "APLAYER"
        def AGAME = "AGAME"
        playerServices.playerID.set(APLAYER)
        assert services.is(playerServices.gamePlay(AGAME))
        assert services.playerID.get() == APLAYER
        assert services.gameID.get() == AGAME
    }

    void testNullPlayer() {
        playerServices.gamePlayServices = null

        def APLAYER = "APLAYER"
        playerServices.playerID.set(APLAYER)

        OutboundJaxrsResponse resp = playerServices.gamePlay(null)
        assert resp.status == javax.ws.rs.core.Response.Status.BAD_REQUEST.statusCode
        assert resp.entity == "Missing game identity"
    }

    void testEmptyPlayer() {
        playerServices.gamePlayServices = null

        def APLAYER = "APLAYER"
        playerServices.playerID.set(APLAYER)

        OutboundJaxrsResponse resp = playerServices.gamePlay("   ")
        assert resp.status == javax.ws.rs.core.Response.Status.BAD_REQUEST.statusCode
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
        def APLAYER = "APLAYER"
        playerServices.playerID.set(APLAYER)
        def features = [GameFeature.AlternatingPuzzleSetter, GameFeature.DrawGallows] as Set
        def players = ["1", "2", "3"]
        PlayerServices.FeaturesAndPlayers input = new PlayerServices.FeaturesAndPlayers(features: features, players: players)
        MaskedGame game = new MaskedGame()
        playerServices.newGameHandler = [
                handleCreateNewGame: {
                    String i, List<String> p, Set<GameFeature> f ->
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

    void testCreateFindGameAnnotations() {
        def gameServices = PlayerServices.getMethod(
                "gamesForPhase", [
                GamePhase.class,
                Integer.class,
                Integer.class
        ] as Class[])
        assert (gameServices.annotations.size() == 3 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 4)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "games/{phase}"
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(GET.class)
        def params = gameServices.parameterAnnotations
        assert params.length == 3
        assert params[0].length == 1
        assert params[0][0].annotationType() == PathParam.class
        assert ((PathParam) params[0][0]).value() == "phase"
        assert params[1].length == 1
        assert params[1][0].annotationType() == QueryParam.class
        assert ((QueryParam) params[1][0]).value() == "page"
        assert params[2].length == 1
        assert params[2][0].annotationType() == QueryParam.class
        assert ((QueryParam) params[2][0]).value() == "pageSize"
    }

    void testFindGames() {
        List<MaskedGame> games = []
        def APLAYER = "APLAYER"
        playerServices.playerID.set(APLAYER)
        GamePhase.values().each {
            GamePhase gamePhase ->
                playerServices.gameFinderHandler = [
                        findGames: {
                            String pid, GamePhase gp, int p, int ps ->
                                assert pid == APLAYER
                                assert gp == gamePhase
                                assert p == 1
                                assert ps == 20
                                games
                        }
                ] as GameFinderHandler
                assert games.is(playerServices.gamesForPhase(gamePhase, 1, 20))
        }
    }

    void testFindGamesDefaults() {
        List<MaskedGame> games = []
        def APLAYER = "APLAYER"
        playerServices.playerID.set(APLAYER)
        GamePhase.values().each {
            GamePhase gamePhase ->
                playerServices.gameFinderHandler = [
                        findGames: {
                            String pid, GamePhase gp, int p, int ps ->
                                assert pid == APLAYER
                                assert gp == gamePhase
                                assert p == PlayerServices.DEFAULT_PAGE
                                assert ps == PlayerServices.DEFAULT_PAGE_SIZE
                                games
                        }
                ] as GameFinderHandler
                assert games.is(playerServices.gamesForPhase(gamePhase, null, null))
        }
    }

    void testPlayerInfo() {
        Player p = new Player(id: "x", displayName: "y", disabled: true, source: "here");
        playerServices.playerRepository = [
                findOne: {
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
        playerServices.playerID.set('PID')
        playerServices.friendFinder = [
                findFriends: {
                    String it ->
                        assert it == 'PID'
                        return ['1': '2', '3': '4', '5': '6']
                }
        ] as FriendFinder

        assert playerServices.getFriends() == ['1': '2', '3': '4', '5': '6']
    }
}
