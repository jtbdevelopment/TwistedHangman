package com.jtbdevelopment.TwistedHangman.game.state

/**
 * Date: 11/14/14
 * Time: 12:34 PM
 */
enum GamePhase {
    Challenge,  /*  Agreement from initial players  */
    Declined,  /*  Challenge was rejected by a player */
    Quit,  /*  Player Quit, similar to Declined but after game started  */
    Setup, /*  Setting word phrases  */
    Playing,
    Rematch,  /*  Option to continue to a new game  */
    Rematched,
}