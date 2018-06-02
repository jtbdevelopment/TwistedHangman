package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.TypeChecked
import org.junit.Assert

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

    void testGetFeatures() {
        Assert.assertEquals([
                (GameFeature.DrawFace)               : GameFeature.DrawFace.description,
                (GameFeature.DrawGallows)            : GameFeature.DrawGallows.description,
                (GameFeature.TurnBased)              : GameFeature.TurnBased.description,
                (GameFeature.Live)                   : GameFeature.Live.description,
                (GameFeature.SingleWinner)           : GameFeature.SingleWinner.description,
                (GameFeature.AllComplete)            : GameFeature.AllComplete.description,
                (GameFeature.AlternatingPuzzleSetter): GameFeature.AlternatingPuzzleSetter.description,
                (GameFeature.SystemPuzzles)          : GameFeature.SystemPuzzles.description,
                (GameFeature.Head2Head)              : GameFeature.Head2Head.description,
                (GameFeature.Thieving)               : GameFeature.Thieving.description
        ], playerGatewayService.featuresAndDescriptions())
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

}

