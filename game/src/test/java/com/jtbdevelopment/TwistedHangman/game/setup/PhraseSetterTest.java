package com.jtbdevelopment.TwistedHangman.game.setup;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 11/7/14 Time: 6:48 AM
 */
public class PhraseSetterTest {

  private static String inputWordPhrase = "Bohemian Rhapsody";
  private static String inputCategory = "Song";
  private static String expectedWordPhrase = inputWordPhrase.toUpperCase();
  private static String expectedCategory = inputCategory.toUpperCase();
  private PhraseFeatureInitializer initializer1 = Mockito.mock(PhraseFeatureInitializer.class);
  private PhraseFeatureInitializer initializer2 = Mockito.mock(PhraseFeatureInitializer.class);
  private PhraseFeatureInitializer initializer3 = Mockito.mock(PhraseFeatureInitializer.class);
  private PhraseSetter phraseSetter = new PhraseSetter(
      Arrays.asList(initializer1, initializer2, initializer3));

  @Test
  public void testPhraseSetting() {
    IndividualGameState gameState = new IndividualGameState(new HashSet<>());

    phraseSetter.setWordPhrase(gameState, inputWordPhrase, inputCategory);
    assertEquals(expectedCategory, gameState.getCategory());
    assertArrayEquals(expectedWordPhrase.toCharArray(), gameState.getWordPhrase());
    verify(initializer3).initializeForPhrase(gameState);
    verify(initializer2).initializeForPhrase(gameState);
    verify(initializer1).initializeForPhrase(gameState);
  }
}
