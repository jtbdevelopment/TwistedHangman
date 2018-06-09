package com.jtbdevelopment.TwistedHangman.game.setup.featureinitializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.HashSet;
import org.junit.Test;

/**
 * Date: 11/6/14 Time: 9:05 PM
 */
public class WorkingWordPhraseInitializerTest {

  private WorkingWordPhraseInitializer initializer = new WorkingWordPhraseInitializer();

  @Test
  public void testInitSimplePhrase() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());
    gameState.setWordPhrase("cat".toCharArray());
    initializer.initializeForPhrase(gameState);
    assertEquals("___", gameState.getWorkingWordPhraseString());
    assertEquals(3, gameState.getBlanksRemaining());
  }

  @Test
  public void testInitPunctuation() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());
    gameState.setWordPhrase("'cat'".toCharArray());
    initializer.initializeForPhrase(gameState);
    assertEquals("'___'", gameState.getWorkingWordPhraseString());
    assertEquals(3, gameState.getBlanksRemaining());
  }

  @Test
  public void testNumerics() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());
    gameState.setWordPhrase("1 cat".toCharArray());
    initializer.initializeForPhrase(gameState);
    assertEquals("1 ___", gameState.getWorkingWordPhraseString());
    assertEquals(3, gameState.getBlanksRemaining());
  }
}
