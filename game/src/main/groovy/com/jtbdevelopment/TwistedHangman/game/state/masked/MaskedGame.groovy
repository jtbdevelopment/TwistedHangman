package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.data.annotation.Transient

import java.time.ZonedDateTime

/**
 * Date: 11/2/2014
 * Time: 9:36 PM
 *
 * Represents the Game as masked for a specific player
 */
@CompileStatic
public class MaskedGame implements Cloneable {
    @Transient
    Player maskedForPlayer

    String maskedForPlayerMD5

    String id

    ZonedDateTime created
    ZonedDateTime lastUpdate

    ZonedDateTime declined
    ZonedDateTime completed
    ZonedDateTime rematched

    GamePhase gamePhase

    String initiatingPlayer
    Map<String, String> players = [:]  //  players will be hashed down to an md5 key + displayName
    Map<String, PlayerChallengeState> playerStates = [:]  // key will be md5 key

    Set<GameFeature> features = [] as Set
    Map<GameFeature, Object> featureData = [:]  // Any objects referring to players will be changed to md5

    String wordPhraseSetter  // md5
    Map<String, MaskedIndividualGameState> solverStates = [:]
    // md5 key  - will only contain entries for own game if solving

    Map<String, Integer> playerScores = [:]  // md5 key
}
