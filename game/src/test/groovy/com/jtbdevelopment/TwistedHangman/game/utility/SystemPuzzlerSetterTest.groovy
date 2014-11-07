package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import org.junit.Before
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 9:13 PM
 */
class SystemPuzzlerSetterTest {
    private static final String phrase = "WHO LET THE DOGS OUT?"
    private static final String category = "SONG"
    private static final CannedGame cannedGame = new CannedGame(wordPhrase: phrase, category: category)
    SystemPuzzlerSetter puzzlerSetter = new SystemPuzzlerSetter()

    @Before
    public void setup() {
        puzzlerSetter.randomCannedGameFinder = [getRandomGame: cannedGame] as RandomCannedGameFinder
        puzzlerSetter.phraseSetter = [
                setWordPhrase: {
                    IndividualGameState gameState, String phrase, String category ->
                        assert gameState != null
                        assert phrase == SystemPuzzlerSetterTest.phrase
                        assert category == SystemPuzzlerSetterTest.category
                        gameState.wordPhrase = phrase.toCharArray()
                        gameState.category = category
                }
        ] as PhraseSetter
    }

    @Test
    public void testSystemPuzzler() {
        Game game = new Game()
        game.solverStates = ["1": new IndividualGameState([] as Set),
                             "2": new IndividualGameState([] as Set),
                             "3": new IndividualGameState([] as Set)]
        puzzlerSetter.setWordPhraseFromSystem(game)
        game.solverStates.values().each {
            assert it.category == category
            assert it.wordPhraseString == phrase
        }
    }
}
