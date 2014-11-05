package com.jtbdevelopment.TwistedHangman.factory

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

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
    GameRepository gameRepository

    public Game createGame(
            final Set<GameFeature> features,
            final List<String> players,
            final String initiatingPlayer) {
        Game game = createFreshGame(features, players, initiatingPlayer)

        prepareGame(game)
    }

    public Game createGame(final Game previousGame, final String initiatingPlayer) {
        //  Rotate players
        List<String> players = []
        players.addAll(previousGame.players)
        players.add(players.remove(0))

        Game game = createFreshGame(previousGame.features, players, initiatingPlayer)
        game.previousId = previousGame.id
        game.playerScores.putAll(previousGame.playerScores)

        prepareGame(game)
    }

    protected Game prepareGame(final Game game) {
        initializeGame(game)
        validateGame(game)
        Game saved = gameRepository.save(game)
        if (!StringUtils.isEmpty(saved.previousId)) {
            Game previous = gameRepository.findOne(saved.previousId)
            previous.gamePhase = Game.GamePhase.Rematched
            previous.rematchId = saved.id
            gameRepository.save(previous)
        }
        saved
    }

    protected void initializeGame(final Game game) {
        gameInitializers.each { GameInitializer it -> it.initializeGame(game) }
    }

    protected void validateGame(final Game game) {
        GameValidator find = gameValidators.find { GameValidator it -> !it.validateGame(game) }
        if (find != null) {
            throw new IllegalStateException("Game is not valid somehow.")
        }
    }

    protected Game createFreshGame(final Set<GameFeature> features,
                                   final List<String> players,
                                   final String initiatingPlayer) {

        Game game = new Game()
        game.created = ZonedDateTime.now(ZoneId.of("GMT"))
        game.lastMove = game.created
        game.initiatingPlayer = initiatingPlayer
        game.version = 0
        game.gamePhase = Game.GamePhase.Challenge
        game.features.addAll(features)
        game.players.addAll(players)
        game.wordPhraseSetter = ""
        game
    }
}
