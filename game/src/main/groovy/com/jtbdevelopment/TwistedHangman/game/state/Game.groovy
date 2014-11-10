package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.ZonedDateTime

/**
 * Date: 11/2/2014
 * Time: 9:36 PM
 */
@Document
@CompileStatic
public class Game implements Cloneable {
    public enum GamePhase {
        Challenge,  /*  Agreement from initial players  */
        Declined,  /*  Challenge was rejected by a player */
        Setup, /*  Setting word phrases  */
        Playing,
        Rematch,  /*  Option to continue to a new game  */
        Rematched,
    }

    public enum PlayerChallengeState {
        Pending,
        Accepted,
        Rejected
    }

    @Id
    String id
    @Version
    Integer version

    @Indexed
    @CreatedDate
    ZonedDateTime created
    @Indexed
    @LastModifiedDate
    ZonedDateTime lastMove

    ZonedDateTime declined
    ZonedDateTime completed
    ZonedDateTime rematched

    String previousId
    String rematchId

    @Indexed
    GamePhase gamePhase

    @Indexed
    Player initiatingPlayer
    @Indexed
    LinkedHashSet<Player> players = new LinkedHashSet<>()  //  Ordered by turns, challengers etc
    Map<Player, PlayerChallengeState> playerStates = [:]

    Set<GameFeature> features = [] as Set
    Map<GameFeature, Object> featureData = [:]

    Player wordPhraseSetter
    Map<Player, IndividualGameState> solverStates = [:]

    Map<Player, Integer> playerScores = [:]

    boolean equals(final o) {
        if (this.is(o)) return true
        if (!(o instanceof Game)) return false

        final Game game = (Game) o

        if (id != game.id) return false

        return true
    }

    int hashCode() {
        return id.hashCode()
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", created=" + created +
                ", declined=" + declined +
                ", lastMove=" + lastMove +
                ", completed=" + completed +
                ", rematched=" + rematched +
                ", previousId='" + previousId + '\'' +
                ", rematchId='" + rematchId + '\'' +
                ", gamePhase=" + gamePhase +
                ", initiatingPlayer=" + initiatingPlayer +
                ", players=" + players +
                ", playerStates=" + playerStates +
                ", features=" + features +
                ", featureData=" + featureData +
                ", wordPhraseSetter=" + wordPhraseSetter +
                ", solverStates=" + solverStates +
                ", playerScores=" + playerScores +
                '}';
    }
}
