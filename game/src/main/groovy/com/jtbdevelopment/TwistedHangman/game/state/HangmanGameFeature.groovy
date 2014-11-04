package com.jtbdevelopment.TwistedHangman.game.state

/**
 * Date: 11/3/14
 * Time: 6:45 AM
 */
public enum HangmanGameFeature {
    Thieving("Stealing Allowed"),
    ThievingCountTracking(true),
    ThievingPositionTracking(true),

    TurnBased(""),

    SystemChallenger,
    AlternatingChallenger,

    SingleWinner,

    TwoPlayersOnly(true)

    final boolean internal
    final String description


    public HangmanGameFeature(final String description, boolean internal = false) {
        this.internal = internal
        this.description = description
    }

    public HangmanGameFeature(boolean internal = false) {
        this("", internal)
    }

    static final List<Set<HangmanGameFeature>> ALLOWED_COMBINATIONS = [
            //  TWO PLAYER ONLY
            //  Two players playing regular hangman against each other with their phrases, live, multiple winners
            [TwoPlayersOnly] as Set,
            //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
            [TwoPlayersOnly, TurnBased] as Set,
            //  Two players playing thieving hangman against each other with their phrases, live, multiple winners
            [TwoPlayersOnly, Thieving] as Set,
            //  Two players playing regular hangman against each other with their phrases, live, single winners
            [TwoPlayersOnly, SingleWinner] as Set,
            //  Two players playing thieving hangman against each other with their phrases, live, single winners
            [TwoPlayersOnly, SingleWinner, Thieving] as Set,
            //  Two players playing regular hangman against each other with their phrases, turns, single winners
            [TwoPlayersOnly, SingleWinner, TurnBased] as Set,
            //  Two players playing thieving hangman against each other with their phrases, turns, single winners
            [TwoPlayersOnly, SingleWinner, Thieving, TurnBased] as Set,

            //  Multiple Players Not Using System ID - take turns against phrases
            //  Two players playing regular hangman against each other with their phrases, live, multiple winners
            [AlternatingChallenger] as Set,
            //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
            [AlternatingChallenger, TurnBased] as Set,
            //  Two players playing thieving hangman against each other with their phrases, live, multiple winners
            [AlternatingChallenger, Thieving] as Set,
            //  Two players playing regular hangman against each other with their phrases, live, single winners
            [AlternatingChallenger, SingleWinner] as Set,
            //  Two players playing thieving hangman against each other with their phrases, live, single winners
            [AlternatingChallenger, SingleWinner, Thieving] as Set,
            //  Two players playing regular hangman against each other with their phrases, turns, single winners
            [AlternatingChallenger, SingleWinner, TurnBased] as Set,
            //  Two players playing thieving hangman against each other with their phrases, turns, single winners
            [AlternatingChallenger, SingleWinner, Thieving, TurnBased] as Set,

            //  One or more players playing regular hangman against system phrase, live, multiple winners
            [SystemChallenger] as Set,
            //  One or more players playing regular hangman against system phrase, turns, multiple winners
            [SystemChallenger, TurnBased] as Set,
            //  One or more players playing thieving hangman against system phrase, live, multiple winners
            [SystemChallenger, Thieving] as Set,
            //  One or more players playing thieving hangman against system phrase, turns, multiple winners
            [SystemChallenger, Thieving, TurnBased] as Set,
            //  One or more players playing regular hangman against system phrase, live, first to solve wins
            [SystemChallenger, SingleWinner] as Set,
            //  One or more players playing thieving hangman against system phrase, live, first to solve wins
            [SystemChallenger, SingleWinner, Thieving] as Set,
            //  One or more players playing thieving hangman against system phrase, turns, first to solve wins
            [SystemChallenger, SingleWinner, Thieving, TurnBased] as Set,
    ]
}