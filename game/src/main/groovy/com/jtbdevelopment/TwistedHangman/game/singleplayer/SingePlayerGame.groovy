package com.jtbdevelopment.TwistedHangman.game.singleplayer

import com.jtbdevelopment.TwistedHangman.game.Game
import com.jtbdevelopment.TwistedHangman.game.Player
import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameState
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 11/2/2014
 * Time: 9:34 PM
 */
@Document
class SingePlayerGame extends Game {
    Player player

    HangmanGameState gameState
}
