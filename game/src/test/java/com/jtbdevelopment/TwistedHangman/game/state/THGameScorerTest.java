package com.jtbdevelopment.TwistedHangman.game.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 11/8/2014 Time: 6:57 PM
 */
public class THGameScorerTest extends TwistedHangmanTestCase {

  private THGameScorer gameScorer = new THGameScorer();

  @Test
  public void testScoreForMultiWinMultiPlayerSystemGame() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFOUR));
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    map.put(PTWO.getId(), -2);
    map.put(PTHREE.getId(), 4);
    map.put(PFOUR.getId(), 0);
    game.setPlayerRunningScores(map);
    Map<ObjectId, IndividualGameState> map1 = new HashMap<>();
    map1.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map1.put(PTWO.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map1.put(PTHREE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map1.put(PFOUR.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map1);
    assertSame(game, gameScorer.scoreGame(game));
    assertNull(game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(0, (int) game.getPlayerRunningScores().get(PONE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PTWO.getId()));
    assertEquals(5, (int) game.getPlayerRunningScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PFOUR.getId()));

    assertEquals(-1, (int) game.getPlayerRoundScores().get(PONE.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTWO.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRoundScores().get(PFOUR.getId()));
  }

  @Test
  public void testScoreForMultiWinMultiPlayerAlternatingGame() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFOUR));
    game.setWordPhraseSetter(PTHREE.getId());
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    map.put(PTWO.getId(), -2);
    map.put(PTHREE.getId(), 4);
    map.put(PFOUR.getId(), 0);
    game.setPlayerRunningScores(map);
    Map<ObjectId, IndividualGameState> map1 = new HashMap<>();
    map1.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map1.put(PTWO.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map1.put(PFOUR.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map1);
    assertSame(game, gameScorer.scoreGame(game));
    assertNull(game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(0, (int) game.getPlayerRunningScores().get(PONE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PTWO.getId()));
    assertEquals(5, (int) game.getPlayerRunningScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PFOUR.getId()));

    assertEquals(-1, (int) game.getPlayerRoundScores().get(PONE.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTWO.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRoundScores().get(PFOUR.getId()));
  }

  @Test
  public void testScoreForMultiWinTwoPlayerHeadToHead() {
    THGame game = new THGame();
    game.setPlayers(Arrays.asList(PONE, PTWO));
    game.setWordPhraseSetter(null);
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    map.put(PTWO.getId(), -2);
    game.setPlayerRunningScores(map);
    Map<ObjectId, Integer> map1 = new HashMap<>();
    map1.put(PONE.getId(), 0);
    map1.put(PTWO.getId(), 0);
    game.setPlayerRoundScores(map1);
    Map<ObjectId, IndividualGameState> map2 = new HashMap<>();
    map2.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map2.put(PTWO.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map2);
    assertSame(game, gameScorer.scoreGame(game));
    assertNull(game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(2, (int) game.getPlayerRunningScores().get(PONE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PTWO.getId()));

    assertEquals(1, (int) game.getPlayerRoundScores().get(PONE.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTWO.getId()));
  }

  @Test
  public void testScoreForSingleWinMultiPlayerSystemGame() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.SingleWinner);
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFOUR));
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    map.put(PTWO.getId(), -2);
    map.put(PTHREE.getId(), 4);
    map.put(PFOUR.getId(), 0);
    game.setPlayerRunningScores(map);
    Map<ObjectId, Integer> map1 = new HashMap<>();
    map1.put(PONE.getId(), 0);
    map1.put(PTWO.getId(), 0);
    map1.put(PTHREE.getId(), 0);
    map1.put(PFOUR.getId(), 0);
    game.setPlayerRoundScores(map1);
    Map<ObjectId, IndividualGameState> map2 = new HashMap<>();
    map2.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return false;
      }
    });
    map2.put(PTWO.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map2.put(PTHREE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return false;
      }
    });
    map2.put(PFOUR.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map2);
    assertSame(game, gameScorer.scoreGame(game));
    assertEquals(PTWO.getId(), game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(1, (int) game.getPlayerRunningScores().get(PONE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PTWO.getId()));
    assertEquals(4, (int) game.getPlayerRunningScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PFOUR.getId()));

    assertEquals(0, (int) game.getPlayerRoundScores().get(PONE.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTWO.getId()));
    assertEquals(0, (int) game.getPlayerRoundScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRoundScores().get(PFOUR.getId()));
  }

  @Test
  public void testScoreForSingleWinMultiPlayerAlternatingGame() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.SingleWinner);
    game.setPlayers(Arrays.asList(PONE, PTWO, PTHREE, PFOUR));
    game.setWordPhraseSetter(PTHREE.getId());
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    map.put(PTWO.getId(), -2);
    map.put(PTHREE.getId(), 4);
    map.put(PFOUR.getId(), 0);
    game.setPlayerRunningScores(map);
    Map<ObjectId, Integer> map1 = new HashMap<>();
    map1.put(PONE.getId(), 0);
    map1.put(PTWO.getId(), 0);
    map1.put(PTHREE.getId(), 0);
    map1.put(PFOUR.getId(), 0);
    game.setPlayerRoundScores(map1);
    Map<ObjectId, IndividualGameState> map2 = new HashMap<>();
    map2.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return false;
      }
    });
    map2.put(PTWO.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map2.put(PFOUR.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map2);
    assertSame(game, gameScorer.scoreGame(game));
    assertEquals(PTWO.getId(), game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(1, (int) game.getPlayerRunningScores().get(PONE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PTWO.getId()));
    assertEquals(4, (int) game.getPlayerRunningScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRunningScores().get(PFOUR.getId()));

    assertEquals(0, (int) game.getPlayerRoundScores().get(PONE.getId()));
    assertEquals(1, (int) game.getPlayerRoundScores().get(PTWO.getId()));
    assertEquals(0, (int) game.getPlayerRoundScores().get(PTHREE.getId()));
    assertEquals(-1, (int) game.getPlayerRoundScores().get(PFOUR.getId()));
  }

  @Test
  public void testScoreForSingleWinTwoPlayerHeadToHead() {
    THGame game = new THGame();
    game.getFeatures().add(GameFeature.SingleWinner);
    game.setPlayers(Arrays.asList(PONE, PTWO));
    game.setWordPhraseSetter(null);
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    map.put(PTWO.getId(), -2);
    game.setPlayerRunningScores(map);
    Map<ObjectId, Integer> map1 = new HashMap<>();
    map1.put(PONE.getId(), 0);
    map1.put(PTWO.getId(), 0);
    game.setPlayerRoundScores(map1);
    Map<ObjectId, IndividualGameState> map2 = new HashMap<>();
    map2.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    map2.put(PTWO.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return false;
      }
    });
    game.setSolverStates(map2);
    assertSame(game, gameScorer.scoreGame(game));
    assertEquals(PONE.getId(), game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(2, (int) game.getPlayerRunningScores().get(PONE.getId()));
    assertEquals(-2, (int) game.getPlayerRunningScores().get(PTWO.getId()));

    assertEquals(1, (int) game.getPlayerRoundScores().get(PONE.getId()));
    assertEquals(0, (int) game.getPlayerRoundScores().get(PTWO.getId()));
  }

  @Test
  public void testScoreWinForSinglePlayerGame() {
    THGame game = new THGame();
    game.setPlayers(Collections.singletonList(PONE));
    game.setWordPhraseSetter(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId());
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    game.setPlayerRunningScores(map);
    Map<ObjectId, IndividualGameState> map1 = new HashMap<>();
    map1.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return true;
      }

      public boolean isPlayerHung() {
        return false;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map1);
    assertSame(game, gameScorer.scoreGame(game));
    assertNull(game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(2, (int) game.getPlayerRunningScores().get(PONE.getId()));

    assertEquals(1, (int) game.getPlayerRoundScores().get(PONE.getId()));
  }

  @Test
  public void testLossWinForSinglePlayerGame() {
    THGame game = new THGame();
    game.setPlayers(Collections.singletonList(PONE));
    game.setWordPhraseSetter(TwistedHangmanSystemPlayerCreator.TH_PLAYER.getId());
    Map<ObjectId, Integer> map = new HashMap<>();
    map.put(PONE.getId(), 1);
    game.setPlayerRunningScores(map);
    Map<ObjectId, IndividualGameState> map1 = new HashMap<>();
    map1.put(PONE.getId(), new IndividualGameState() {
      public boolean isPuzzleSolved() {
        return false;
      }

      public boolean isPlayerHung() {
        return true;
      }

      public boolean isPuzzleOver() {
        return true;
      }
    });
    game.setSolverStates(map1);
    assertSame(game, gameScorer.scoreGame(game));
    assertNull(game.getFeatureData().get(GameFeature.SingleWinner));
    assertEquals(0, (int) game.getPlayerRunningScores().get(PONE.getId()));

    assertEquals(-1, (int) game.getPlayerRoundScores().get(PONE.getId()));
  }
}
