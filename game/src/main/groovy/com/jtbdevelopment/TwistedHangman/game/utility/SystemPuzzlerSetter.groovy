package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/4/2014
 * Time: 9:34 PM
 */
@CompileStatic
@Component
class SystemPuzzlerSetter {
    private static final Logger logger = LoggerFactory.getLogger(SystemPuzzlerSetter.class)
    @Autowired
    RandomCannedGameFinder randomCannedGameFinder
    @Autowired
    PhraseSetter phraseSetter
    @Autowired
    GameRepository gameRepository

    public Game setWordPhraseFromSystem(final Game game) {
        if (game.features.contains(GameFeature.SystemPuzzles)) {
            CannedGame cannedGame = randomCannedGameFinder.getRandomGame()
            logger.info("System Challenger setting for " + game.id + " using canned id " + cannedGame.id)
            game.solverStates.values().each {
                IndividualGameState gameState ->
                    phraseSetter.setWordPhrase(gameState, cannedGame.wordPhrase, cannedGame.category)
            }
            return gameRepository.save(game)
        }
        return game
    }
}
