package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.games.games.AbstractMultiPlayerGame
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

import java.time.ZonedDateTime

/**
 * Date: 11/2/2014
 * Time: 9:36 PM
 */

@Document(collection = "game")
@CompileStatic
@CompoundIndexes([
        @CompoundIndex(name = "player_phase", def = "{'players._id': 1, 'gamePhase': 1, 'lastUpdate': 1}"),
])
public class Game extends AbstractMultiPlayerGame<ObjectId> implements Cloneable {
    @Id
    ObjectId id

    ZonedDateTime rematchTimestamp

    ObjectId previousId

    int round;

    GamePhase gamePhase

    Set<GameFeature> features = [] as Set
    Map<GameFeature, Object> featureData = [:]

    ObjectId wordPhraseSetter
    Map<ObjectId, IndividualGameState> solverStates = [:]

    Map<ObjectId, Integer> playerRoundScores = [:]
    Map<ObjectId, Integer> playerRunningScores = [:]

    String getIdAsString() {
        if (id) {
            return id.toHexString()
        }
        return null;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", created=" + created +
                ", declinedTimestamp=" + declinedTimestamp +
                ", lastUpdate=" + lastUpdate +
                ", completedTimestamp=" + completedTimestamp +
                ", rematchTimestamp=" + rematchTimestamp +
                ", previousId='" + previousId + '\'' +
                ", gamePhase=" + gamePhase +
                ", initiatingPlayer=" + initiatingPlayer +
                ", players=" + players +
                ", playerStates=" + playerStates +
                ", features=" + features +
                ", featureData=" + featureData +
                ", wordPhraseSetter=" + wordPhraseSetter +
                ", solverStates=" + solverStates +
                ", playerRunningScores=" + playerRunningScores +
                '}';
    }
}
