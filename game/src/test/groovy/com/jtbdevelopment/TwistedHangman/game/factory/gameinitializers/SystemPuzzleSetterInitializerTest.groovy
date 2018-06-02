package com.jtbdevelopment.TwistedHangman.game.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.setup.PhraseSetter
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle
import com.jtbdevelopment.TwistedHangman.game.utility.RandomCannedGameFinder
import com.jtbdevelopment.games.factory.GameInitializer
import org.bson.types.ObjectId
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * Date: 11/6/14
 * Time: 9:13 PM
 */
class SystemPuzzleSetterInitializerTest extends TwistedHangmanTestCase {
    private static final String phrase = "WHO LET THE DOGS OUT?"
    private static final String category = "SONG"
    private static
    final PreMadePuzzle cannedGame = new PreMadePuzzle(wordPhrase: phrase, category: category)
    private RandomCannedGameFinder cannedGameFinder = Mockito.mock(RandomCannedGameFinder.class);
    private PhraseSetter phraseSetter = Mockito.mock(PhraseSetter.class);
    private SystemPuzzleSetterInitializer puzzlerSetter = new SystemPuzzleSetterInitializer(cannedGameFinder, phraseSetter)

    @Before
    void setUp() {
        Mockito.when(cannedGameFinder.getRandomGame()).thenReturn(cannedGame)
    }

    @Test
    void testOrder() {
        assert (GameInitializer.LATE_ORDER + 100) == puzzlerSetter.order
    }

    @Test
    void testSystemPuzzler() {
        puzzlerSetter
        Game game = new Game()
        game.features.add(GameFeature.SystemPuzzles)

        game.setSolverStates([(new ObjectId()): new IndividualGameState([] as Set),
                              (new ObjectId()): new IndividualGameState([] as Set),
                              (new ObjectId()): new IndividualGameState([] as Set)])
        puzzlerSetter.initializeGame(game)

        game.solverStates.values().each {
            Mockito.verify(phraseSetter).setWordPhrase(it, phrase, category)
        }
    }

    @Test
    void testNonSystemPuzzler() {
        Game game = new Game()
        game.setSolverStates([(new ObjectId()): new IndividualGameState([] as Set),
                              (new ObjectId()): new IndividualGameState([] as Set),
                              (new ObjectId()): new IndividualGameState([] as Set)])
        puzzlerSetter.initializeGame(game)
        game.solverStates.values().each {
            Mockito.verify(phraseSetter, Mockito.never()).setWordPhrase(it, phrase, category)
        }
    }
}
