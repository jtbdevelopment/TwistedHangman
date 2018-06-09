package com.jtbdevelopment.TwistedHangman.game.state;

import com.jtbdevelopment.games.mongo.state.AbstractMongoMultiPlayerGame;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Date: 11/2/2014 Time: 9:36 PM
 */
@Document(collection = "game")
@CompoundIndexes({
    @CompoundIndex(name = "player_phase", def = "{'players._id': 1, 'gamePhase': 1, 'lastUpdate': 1}"),
    @CompoundIndex(name = "created", def = "{'created': 1}"),
    @CompoundIndex(name = "lastUpdated", def = "{'lastUpdate': 1}")})
public class THGame extends AbstractMongoMultiPlayerGame<GameFeature> implements Cloneable {

  private ObjectId wordPhraseSetter;
  private Map<ObjectId, IndividualGameState> solverStates = new HashMap<>();
  private Map<ObjectId, Integer> playerRoundScores = new HashMap<>();
  private Map<ObjectId, Integer> playerRunningScores = new HashMap<>();
  private Map<GameFeature, Object> featureData = new HashMap<>();

  public ObjectId getWordPhraseSetter() {
    return wordPhraseSetter;
  }

  public void setWordPhraseSetter(ObjectId wordPhraseSetter) {
    this.wordPhraseSetter = wordPhraseSetter;
  }

  public Map<ObjectId, IndividualGameState> getSolverStates() {
    return solverStates;
  }

  public void setSolverStates(Map<ObjectId, IndividualGameState> solverStates) {
    this.solverStates = solverStates;
  }

  public Map<ObjectId, Integer> getPlayerRoundScores() {
    return playerRoundScores;
  }

  public void setPlayerRoundScores(Map<ObjectId, Integer> playerRoundScores) {
    this.playerRoundScores = playerRoundScores;
  }

  public Map<ObjectId, Integer> getPlayerRunningScores() {
    return playerRunningScores;
  }

  public void setPlayerRunningScores(Map<ObjectId, Integer> playerRunningScores) {
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
