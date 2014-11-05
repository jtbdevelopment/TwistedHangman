package com.jtbdevelopment.TwistedHangman.game

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
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
public class Game {
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
    ZonedDateTime created
    ZonedDateTime declined
    @Indexed
    ZonedDateTime lastMove
    @Indexed
    ZonedDateTime completed
    @Indexed
    ZonedDateTime rematched

    String previousId
    String rematchId

    @Indexed
    GamePhase gamePhase

    @Indexed
    String initiatingPlayer
    List<String> players = []  //  Ordered by turns, challengers etc
    Map<String, PlayerChallengeState> playerStates = [:]

    Set<HangmanGameFeature> features = [] as Set
    Map<HangmanGameFeature, Object> featureData = [:]

    String wordPhraseSetter
    Map<String, HangmanGameState> solverStates = [:]

    Map<String, Integer> playerScores = [:]

    public <T> T getFeatureData(final HangmanGameFeature feature) {
        (T) featureData[feature]
    }
}
