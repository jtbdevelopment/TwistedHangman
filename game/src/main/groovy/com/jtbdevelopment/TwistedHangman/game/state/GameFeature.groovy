package com.jtbdevelopment.TwistedHangman.game.state

import groovy.transform.CompileStatic

/**
 * Date: 11/3/14
 * Time: 6:45 AM
 */
@CompileStatic
public enum GameFeature {
    Thieving("Stealing Allowed"),
    ThievingCountTracking("Stolen Letter Count", true, false),
    ThievingPositionTracking("Stolen Letter Markers", true, false),

    TurnBased("Turn-based guessing."),

    SystemPuzzles("Computer provides puzzles."),
    AlternatingPuzzleSetter("Take turns setting the puzzle and watching others."),

    SingleWinner("Only one winner."),

    TwoPlayersOnly("2 Player Limit.", true, true),

    DrawGallows("Draw gallows.", false, true),
    DrawFace("Draw face.", false, true)

    final boolean internal
    final boolean validate
    final String description


    public GameFeature(final String description, boolean internal = false, validate = true) {
        this.internal = internal
        this.description = description
        this.validate = validate
    }

    static final List<Set<GameFeature>> ALLOWED_COMBINATIONS
    static {
        ALLOWED_COMBINATIONS = [
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
                [AlternatingPuzzleSetter] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
                [AlternatingPuzzleSetter, TurnBased] as Set,
                //  Two players playing thieving hangman against each other with their phrases, live, multiple winners
                [AlternatingPuzzleSetter, Thieving] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, single winners
                [AlternatingPuzzleSetter, SingleWinner] as Set,
                //  Two players playing thieving hangman against each other with their phrases, live, single winners
                [AlternatingPuzzleSetter, SingleWinner, Thieving] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, single winners
                [AlternatingPuzzleSetter, SingleWinner, TurnBased] as Set,
                //  Two players playing thieving hangman against each other with their phrases, turns, single winners
                [AlternatingPuzzleSetter, SingleWinner, Thieving, TurnBased] as Set,

                //  One or more players playing regular hangman against system phrase, live, multiple winners
                [SystemPuzzles] as Set,
                //  One or more players playing regular hangman against system phrase, turns, multiple winners
                [SystemPuzzles, TurnBased] as Set,
                //  One or more players playing thieving hangman against system phrase, live, multiple winners
                [SystemPuzzles, Thieving] as Set,
                //  One or more players playing thieving hangman against system phrase, turns, multiple winners
                [SystemPuzzles, Thieving, TurnBased] as Set,
                //  One or more players playing regular hangman against system phrase, live, first to solve wins
                [SystemPuzzles, SingleWinner] as Set,
                //  One or more players playing thieving hangman against system phrase, live, first to solve wins
                [SystemPuzzles, SingleWinner, Thieving] as Set,
                //  One or more players playing thieving hangman against system phrase, turns, first to solve wins
                [SystemPuzzles, SingleWinner, Thieving, TurnBased] as Set,
        ]
    }
}