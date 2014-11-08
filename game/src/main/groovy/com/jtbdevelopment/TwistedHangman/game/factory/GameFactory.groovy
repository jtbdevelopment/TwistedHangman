package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.FailedToCreateValidGameException
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/3/14
 * Time: 9:24 PM
 */
@Component
@CompileStatic
class GameFactory {
    @Autowired
    List<FeatureExpander> featureExpanders
    @Autowired
    List<GameInitializer> gameInitializers
    @Autowired
    List<GameValidator> gameValidators
    @Autowired
    List<IndividualGamesInitializer> individualGamesInitializers
    @Autowired
    GameRepository gameRepository

    public Game createGame(
            final Set<GameFeature> features,
            final List<Player> players,
            final Player initiatingPlayer) {
        Game game = createFreshGame(features, players, initiatingPlayer)

        prepareGame(game)
    }

    public Game createGame(final Game previousGame, final Player initiatingPlayer) {
        List<Player> players = rotatePlayers(previousGame)

        Game game = createFreshGame(previousGame.features, players, initiatingPlayer)
        game.previousId = previousGame.id
        game.playerScores.putAll(previousGame.playerScores)

        prepareGame(game)
    }

    private List<Player> rotatePlayers(Game previousGame) {
        //  Rotate players
        List<Player> players = []
        players.addAll(previousGame.players)
        players.add(players.remove(0))
        players
    }

    private Game prepareGame(final Game game) {
        initializeGame(game)
        validateGame(game)
        game
    }

    private void initializeGame(final Game game) {
        featureExpanders.each {
            FeatureExpander it ->
                it.enhanceFeatureSet(game.features, game.players)
        }
        gameInitializers.each {
            GameInitializer it ->
                it.initializeGame(game)
        }
        individualGamesInitializers.each {
            IndividualGamesInitializer it ->
                it.initializeIndividualGameStates(game)
        }
    }

    private void validateGame(final Game game) {
        Collection<GameValidator> invalid = gameValidators.findAll { GameValidator it -> !it.validateGame(game) }
        if (!invalid.empty) {
            StringBuilder error = new StringBuilder()
            invalid.each { GameValidator gameValidator -> error.append(gameValidator.errorMessage()).append("  ") }
            throw new FailedToCreateValidGameException(error.toString())
        }
    }

    private Game createFreshGame(final Set<GameFeature> features,
                                 final List<Player> players,
                                 final Player initiatingPlayer) {
        Game game = new Game()
        game.created = ZonedDateTime.now(ZoneId.of("GMT"))
        game.lastMove = game.created
        game.initiatingPlayer = initiatingPlayer
        game.version = null
        game.gamePhase = Game.GamePhase.Challenge
        game.features.addAll(features)
        game.players.addAll(players)
        if (!game.players.contains(initiatingPlayer)) {
            game.players.add(initiatingPlayer)
        }
        game.wordPhraseSetter = null
        game
    }
}
