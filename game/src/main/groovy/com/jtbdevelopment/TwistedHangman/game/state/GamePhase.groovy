package com.jtbdevelopment.TwistedHangman.game.state

/**
 * Date: 11/14/14
 * Time: 12:34 PM
 */
enum GamePhase {
    Challenged('Challenge delivered.', 'Challenges'),  /*  Agreement from initial players  */
    Declined('Challenge declined.', 'Declined'),  /*  Challenged was rejected by a player */
    Quit('Game quit.', 'Quit'),  /*  Player Quit, similar to Declined but after game started  */
    Setup('Game setup in progress.', 'Setting Up'), /*  Setting word phrases  */
    Playing('Game in play!', 'Playing'),
    RoundOver('Round finished.', 'Played'),  /*  Option to continue to a new game  */
    NextRoundStarted('Next round begun.', 'Finished Rounds')

    String description
    String groupLabel

    GamePhase(final String description, final String groupLabel) {
        this.description = description
        this.groupLabel = groupLabel
    }

}