package com.jtbdevelopment.TwistedHangman.game.state

/**
 * Date: 11/14/14
 * Time: 12:34 PM
 */
enum GamePhase {
    Challenged('Challenge delivered.', 'Challenges'),  /*  Agreement from initial players  */
    Declined('Challenge declined.', 'Declined', 7),  /*  Challenged was rejected by a player */
    Quit('Game quit.', 'Quit Rounds', 7),  /*  Player Quit, similar to Declined but after game started  */
    Setup('Game setup in progress.', 'Setting Up'), /*  Setting word phrases  */
    Playing('Game in play!', 'Play!'),
    RoundOver('Round finished.', 'Rounds Played', 7),  /*  Option to continue to a new game  */
    NextRoundStarted('Next round begun.', 'Rounds Finished', 7)

    String description
    String groupLabel
    int historyCutoffDays  //  When querying for games, how far back do we go (positive days)

    GamePhase(final String description, final String groupLabel, int historyCutoffDays = 30) {
        this.description = description
        this.groupLabel = groupLabel
        this.historyCutoffDays = historyCutoffDays
    }

}