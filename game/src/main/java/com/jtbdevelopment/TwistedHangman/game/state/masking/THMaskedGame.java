package com.jtbdevelopment.TwistedHangman.game.state.masking;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.state.masking.AbstractMaskedMultiPlayerGame;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 11/2/2014 Time: 9:36 PM
 *
 * Represents the Game as masked for a specific player
 */
public class THMaskedGame extends AbstractMaskedMultiPlayerGame<GameFeature> implements Cloneable {

  private String wordPhraseSetter;
  private Map<String, MaskedIndividualGameState> solverStates = new HashMap<>();
  private Map<String, Integer> playerRoundScores = new HashMap<>();
  private Map<String, Integer> playerRunningScores = new HashMap<>();
  private Map<GameFeature, Object> featureData = new HashMap<>();

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

  public Map<GameFeature, Object> getFeatureData() {
    return featureData;
  }

  public <T> T getFeatureData(final GameFeature feature) {
    return (T) featureData.get(feature);
  }

  public void setFeatureData(Map<GameFeature, Object> featureData) {
    this.featureData = featureData;
  }
}
