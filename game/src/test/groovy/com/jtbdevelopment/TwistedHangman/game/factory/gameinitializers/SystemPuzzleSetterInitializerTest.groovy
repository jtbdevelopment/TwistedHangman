package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle
import com.jtbdevelopment.TwistedHangman.game.utility.RandomCannedGameFinder
import com.jtbdevelopment.games.factory.GameInitializer
import org.junit.Before
import org.springframework.util.StringUtils

/**
 * Date: 11/6/14
 * Time: 9:13 PM
 */
class SystemPuzzleSetterInitializerTest extends TwistedHangmanTestCase {
    private static final String phrase = "WHO LET THE DOGS OUT?"
    private static final String category = "SONG"
    private static final PreMadePuzzle cannedGame = new PreMadePuzzle(wordPhrase: phrase, category: category)
    SystemPuzzleSetterInitializer puzzlerSetter = new SystemPuzzleSetterInitializer()

    @Before
    public void setUp() {
        puzzlerSetter.randomCannedGameFinder = [getRandomGame: cannedGame] as RandomCannedGameFinder
        puzzlerSetter.phraseSetter = [
                setWordPhrase: {
                    IndividualGameState gameState, String phrase, String category ->
                        assert gameState != null
                        assert phrase == SystemPuzzleSetterInitializerTest.phrase
                        assert category == SystemPuzzleSetterInitializerTest.category
                        gameState.wordPhrase = phrase.toCharArray()
                        gameState.category = category
                }
        ] as PhraseSetter
    }

    public void testOrder() {
        assert (GameInitializer.LATE_ORDER + 100) == puzzlerSetter.order
    }

    public void testSystemPuzzler() {
        puzzlerSetter
        Game game = new Game()
        game.features.add(GameFeature.SystemPuzzles)
        game.solverStates = ["1": new IndividualGameState([] as Set),
                             "2": new IndividualGameState([] as Set),
                             "3": new IndividualGameState([] as Set)]
        puzzlerSetter.initializeGame(game)
        game.solverStates.values().each {
            assert it.category == category
            assert it.wordPhraseString == phrase
        }
    }


    public void testNonSystemPuzzler() {
        Game game = new Game()
        game.solverStates = ["1": new IndividualGameState([] as Set),
                             "2": new IndividualGameState([] as Set),
                             "3": new IndividualGameState([] as Set)]
        puzzlerSetter.initializeGame(game)
        game.solverStates.values().each {
            assert StringUtils.isEmpty(it.category)
            assert StringUtils.isEmpty(it.wordPhraseString)
        }
    }
}
