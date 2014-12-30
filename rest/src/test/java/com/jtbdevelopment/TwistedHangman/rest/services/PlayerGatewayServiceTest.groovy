package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.gamecore.mongo.players.MongoPlayer
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerRoles
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import groovy.transform.TypeChecked
import org.bson.types.ObjectId
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/15/2014
 * Time: 11:28 AM
 */
class PlayerGatewayServiceTest extends GroovyTestCase {
    PlayerGatewayService playerGatewayService = new PlayerGatewayService()

    void testClassAnnotations() {
        assert PlayerGatewayService.class.getAnnotation(RolesAllowed.class).value() == [PlayerRoles.PLAYER]
        assert PlayerGatewayService.class.getAnnotation(Path.class).value() == "/"
    }

    void testPing() {
        assert PlayerGatewayService.PING_RESULT == playerGatewayService.ping()
    }

    void testPingAnnotations() {
        def ping = PlayerGatewayService.getMethod("ping", [] as Class[])
        assert (ping.annotations.size() == 3 ||
                (ping.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && ping.annotations.size() == 4)
        )
        assert ping.isAnnotationPresent(GET.class)
        assert ping.isAnnotationPresent(Produces.class)
        assert ping.getAnnotation(Produces.class).value() == [MediaType.TEXT_PLAIN]
        assert ping.isAnnotationPresent(Path.class)
        assert ping.getAnnotation(Path.class).value() == "ping"
    }

    void testValidPlayer() {
        def APLAYER = new ObjectId()
        SecurityContextHolder.context = new SecurityContextImpl()
        SecurityContextHolder.context.authentication = new TestingAuthenticationToken(new SessionUserInfo<ObjectId>() {
            @Override
            Player<ObjectId> getSessionUser() {
                return null
            }

            @Override
            Player<ObjectId> getEffectiveUser() {
                return new MongoPlayer(id: APLAYER)
            }

            @Override
            void setEffectiveUser(final Player<ObjectId> player) {

            }
        }, null)
        PlayerServices services = [playerID: new ThreadLocal<String>()] as PlayerServices
        playerGatewayService.playerServices = services

        assert services.is(playerGatewayService.gameServices())
        assert services.playerID.get() == APLAYER
    }

    void testGameServicesAnnotations() {
        def gameServices = PlayerGatewayService.getMethod("gameServices", [] as Class[])
        assert (gameServices.annotations.size() == 1 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 2)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "player"
        def params = gameServices.parameterAnnotations
        assert params.length == 0
    }

    void testGetFeatures() {
        playerGatewayService.featuresAndDescriptions() == [
                (GameFeature.DrawFace)               : GameFeature.DrawFace.description,
                (GameFeature.DrawGallows)            : GameFeature.DrawGallows.description,
                (GameFeature.TurnBased)              : GameFeature.TurnBased.description,
                (GameFeature.AlternatingPuzzleSetter): GameFeature.AlternatingPuzzleSetter.description,
                (GameFeature.SystemPuzzles)          : GameFeature.SystemPuzzles.description,
                (GameFeature.Thieving)               : GameFeature.Thieving.description
        ]
    }

    void testGetFeaturesAnnotations() {
        def gameServices = PlayerGatewayService.getMethod("featuresAndDescriptions", [] as Class[])
        assert (gameServices.annotations.size() == 3 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 4)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "features"
        assert gameServices.isAnnotationPresent(GET.class)
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        def params = gameServices.parameterAnnotations
        assert params.length == 0
    }

    void testGetPhases() {
        playerGatewayService.phasesAndDescriptions() == [
                (GamePhase.Challenged)      : GamePhase.Challenged.description,
                (GamePhase.Declined)        : GamePhase.Declined.description,
                (GamePhase.NextRoundStarted): GamePhase.NextRoundStarted.description,
                (GamePhase.Playing)         : GamePhase.Playing.description,
                (GamePhase.Quit)            : GamePhase.Quit.description,
                (GamePhase.Setup)           : GamePhase.Setup.description,
                (GamePhase.RoundOver)       : GamePhase.RoundOver.description,
        ]
    }

    void testGetPhasesAnnotations() {
        def gameServices = PlayerGatewayService.getMethod("phasesAndDescriptions", [] as Class[])
        assert (gameServices.annotations.size() == 3 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 4)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "phases"
        assert gameServices.isAnnotationPresent(GET.class)
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        def params = gameServices.parameterAnnotations
        assert params.length == 0
    }
}

