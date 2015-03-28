package com.jtbdevelopment.TwistedHangman.game.state.masking

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.games.state.masking.AbstractMaskedMultiPlayerGame
import groovy.transform.CompileStatic

/**
 * Date: 11/2/2014
 * Time: 9:36 PM
 *
 * Represents the Game as masked for a specific player
 */
@CompileStatic
class MaskedGame extends AbstractMaskedMultiPlayerGame<GameFeature> implements Cloneable {
    Long rematchTimestamp

    Integer round

    GamePhase gamePhase

    String wordPhraseSetter  // md5
    Map<String, MaskedIndividualGameState> solverStates = [:] //md5/state - data will vary based on game phase

    Map<String, Integer> playerRoundScores = [:]  // md5 key
    Map<String, Integer> playerRunningScores = [:]  // md5 key
}
