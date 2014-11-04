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
class Game {
    public enum GamePhase {
        Challenge,  /*  Agreement from initial players  */
        Setup, /*  Setting word phrases  */
        Playing,
        RematchOption,  /*  Option to continue to a new game  */
        Closed,
        Declined,  /*  Challenge was rejected  */
    }

    public enum PlayerChallengeState {
        Pending,
        Accepted,
        Denied
    }

    @Id
    String id
    @Version
    int version

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
    List<String> players  //  Ordered by turns, challenges etc
    Map<String, PlayerChallengeState> playerStates

    Set<HangmanGameFeature> features
    Map<HangmanGameFeature, Object> featureData

    String challengingPlayer
    Map<String, HangmanGameState> solverStates

    Map<String, Integer> playerScores

    public <T> T getFeatureData(final HangmanGameFeature feature) {
        (T) featureData[feature]
    }
}
