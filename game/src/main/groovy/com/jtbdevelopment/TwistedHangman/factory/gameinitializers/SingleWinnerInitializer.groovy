package com.jtbdevelopment.TwistedHangman.factory.gameinitializers

import com.jtbdevelopment.TwistedHangman.factory.GameInitializer
import com.jtbdevelopment.TwistedHangman.game.Game
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 11/4/14
 * Time: 6:39 PM
 */
@Component
@CompileStatic
class SingleWinnerInitializer implements GameInitializer {
    @Override
    void initializeGame(final Game game) {
        if (game.features.contains(HangmanGameFeature.SingleWinner)) {
            game.featureData[HangmanGameFeature.SingleWinner] = ""
        }
    }
}
