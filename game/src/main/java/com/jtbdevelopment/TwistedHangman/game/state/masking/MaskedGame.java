package com.jtbdevelopment.TwistedHangman.game.state.masking;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.state.masking.AbstractMaskedMultiPlayerGame;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 11/2/2014 Time: 9:36 PM
 *
 * Represents the Game as masked for a specific player
 */
public class MaskedGame extends AbstractMaskedMultiPlayerGame<GameFeature> implements Cloneable {

  private String wordPhraseSetter;
  private Map<String, MaskedIndividualGameState> solverStates = new LinkedHashMap<>();
  private Map<String, Integer> playerRoundScores = new LinkedHashMap<>();
  private Map<String, Integer> playerRunningScores = new LinkedHashMap<>();

  public String getWordPhraseSetter() {
    return wordPhraseSetter;
  }

  public void setWordPhraseSetter(String wordPhraseSetter) {
    this.wordPhraseSetter = wordPhraseSetter;
  }

  public Map<String, MaskedIndividualGameState> getSolverStates() {
    return solverStates;
  }

  public void setSolverStates(Map<String, MaskedIndividualGameState> solverStates) {
    this.solverStates = solverStates;
  }

  public Map<String, Integer> getPlayerRoundScores() {
    return playerRoundScores;
  }

  public void setPlayerRoundScores(Map<String, Integer> playerRoundScores) {
    this.playerRoundScores = playerRoundScores;
  }

  public Map<String, Integer> getPlayerRunningScores() {
    return playerRunningScores;
  }

  public void setPlayerRunningScores(Map<String, Integer> playerRunningScores) {
    this.playerRunningScores = playerRunningScores;
  }
}
