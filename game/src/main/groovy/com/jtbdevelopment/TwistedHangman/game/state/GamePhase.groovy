package com.jtbdevelopment.TwistedHangman.game.state

/**
 * Date: 11/14/14
 * Time: 12:34 PM
 */
enum GamePhase {
    Challenged('Challenge delivered.', 'Challenges'),  /*  Agreement from initial players  */
    Declined('Challenge declined.', 'Declined'),  /*  Challenged was rejected by a player */
    Quit('Game quit.', 'Quit Rounds'),  /*  Player Quit, similar to Declined but after game started  */
    Setup('Game setup in progress.', 'Setting Up'), /*  Setting word phrases  */
    Playing('Game in play!', 'Play!'),
    RoundOver('Round finished.', 'Rounds Played'),  /*  Option to continue to a new game  */
    NextRoundStarted('Next round begun.', 'Rounds Finished')

    String description
    String groupLabel

    GamePhase(final String description, final String groupLabel) {
        this.description = description
        this.groupLabel = groupLabel
    }

}