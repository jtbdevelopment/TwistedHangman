package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.NewGameHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import groovy.transform.TypeChecked
import org.bson.types.ObjectId

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/15/2014
 * Time: 12:02 PM
 */
class PlayerServicesTest extends GroovyTestCase {
    PlayerServices playerServices = new PlayerServices()

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

    void testGetGames() {
        def APLAYER = new ObjectId()
        def results = [new Game(), new Game(), new Game()]
        playerServices.playerGamesFinderHandler = [
                findGames: {
                    ObjectId it ->
                        assert it == APLAYER
                        return results
                }
        ] as PlayerGamesFinderHandler
        playerServices.playerID.set(APLAYER)
        assert results.is(playerServices.gamesForPlayer())
    }

    void testGamesAnnotations() {
        def gameServices = PlayerServices.getMethod("gamesForPlayer", [] as Class[])
        assert (gameServices.annotations.size() == 3 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 4)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "games"
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(GET.class)
        def params = gameServices.parameterAnnotations
        assert params.length == 0
    }
}
