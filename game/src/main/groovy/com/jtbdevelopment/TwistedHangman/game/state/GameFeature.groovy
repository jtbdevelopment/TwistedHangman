package com.jtbdevelopment.TwistedHangman.game.state

import groovy.transform.CompileStatic

/**
 * Date: 11/3/14
 * Time: 6:45 AM
 */
@CompileStatic
public enum GameFeature {
    SinglePlayer("Single Player", true, true, true),
    TwoPlayer("2 Players", true, true, true),
    ThreePlus("3+ Players", true, true, true),

    Thieving("For a penalty, steal a letter from the puzzle."),
    ThievingCountTracking("Stolen Letter Count.", true, false),
    ThievingPositionTracking("Stolen Letter Markers.", true, false),

    TurnBased("Guessing and stealing is turn-based instead of live."),

    SystemPuzzles("Twisted Hangman provide the puzzles."),
    AlternatingPuzzleSetter("Take turns setting the puzzle for other players.  The player does not play that round but can see the other players progress."),

    SingleWinner("Only one player can lose.  Game finishes when first person wins or all lose.  Unfinished games are counted as draws."),


    DrawGallows("Draw the gallows too, giving three extra chances.", false, false),
    DrawFace("Draw the face too, giving four extra chances.", false, false)

    final boolean broadCategory
    final boolean internal
    final boolean validate
    final String description


    public GameFeature(final String description, boolean internal = false, validate = true, broadCategory = false) {
        this.internal = internal
        this.description = description
        this.validate = validate
        this.broadCategory = broadCategory
    }

    static final Set<Set<GameFeature>> ALLOWED_COMBINATIONS
    static {
        ALLOWED_COMBINATIONS = [
                //  Single Player
                [SinglePlayer, SystemPuzzles, SingleWinner] as Set,
                [SinglePlayer, SystemPuzzles, TurnBased, SingleWinner] as Set,
                [SinglePlayer, SystemPuzzles, Thieving, TurnBased, SingleWinner] as Set,

                //  TWO PLAYER ONLY
                //  Two players playing regular hangman against each other with their phrases, live, multiple winners
                [TwoPlayer] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
                [TwoPlayer, TurnBased] as Set,
                //  Two players playing thieving hangman against each other with their phrases, live, multiple winners
                [TwoPlayer, Thieving] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, single winners
                [TwoPlayer, SingleWinner] as Set,
                //  Two players playing thieving hangman against each other with their phrases, live, single winners
                [TwoPlayer, SingleWinner, Thieving] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, single winners
                [TwoPlayer, SingleWinner, TurnBased] as Set,
                //  Two players playing thieving hangman against each other with their phrases, turns, single winners
                [TwoPlayer, SingleWinner, Thieving, TurnBased] as Set,
                //  Two players playing regular hangman against system phrase, live, multiple winners
                [TwoPlayer, SystemPuzzles] as Set,
                //  Two players playing regular hangman against system phrase, turns, multiple winners
                [TwoPlayer, SystemPuzzles, TurnBased] as Set,
                //  Two players playing thieving hangman against system phrase, live, multiple winners
                [TwoPlayer, SystemPuzzles, Thieving] as Set,
                //  Two players playing thieving hangman against system phrase, turns, multiple winners
                [TwoPlayer, SystemPuzzles, Thieving, TurnBased] as Set,
                //  Two players playing regular hangman against system phrase, live, first to solve wins
                [TwoPlayer, SystemPuzzles, SingleWinner] as Set,
                //  Two players playing thieving hangman against system phrase, live, first to solve wins
                [TwoPlayer, SystemPuzzles, SingleWinner, Thieving] as Set,
                //  Two players playing thieving hangman against system phrase, turns, first to solve wins
                [TwoPlayer, SystemPuzzles, SingleWinner, Thieving, TurnBased] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, multiple winners
                [TwoPlayer, AlternatingPuzzleSetter] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
                [TwoPlayer, AlternatingPuzzleSetter, TurnBased] as Set,
                //  Two players playing thieving hangman against each other with their phrases, live, multiple winners
                [TwoPlayer, AlternatingPuzzleSetter, Thieving] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, single winners
                [TwoPlayer, AlternatingPuzzleSetter, SingleWinner] as Set,
                //  Two players playing thieving hangman against each other with their phrases, live, single winners
                [TwoPlayer, AlternatingPuzzleSetter, SingleWinner, Thieving] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, single winners
                [TwoPlayer, AlternatingPuzzleSetter, SingleWinner, TurnBased] as Set,
                //  Two players playing thieving hangman against each other with their phrases, turns, single winners
                [TwoPlayer, AlternatingPuzzleSetter, SingleWinner, Thieving, TurnBased] as Set,

                //  Multiple Players Not Using System ID - take turns setting phrases
                //  Three+ players playing regular hangman against each other with their phrases, live, multiple winners
                [ThreePlus, AlternatingPuzzleSetter] as Set,
                //  Three+ players playing regular hangman against each other with their phrases, turns, multiple winners
                [ThreePlus, AlternatingPuzzleSetter, TurnBased] as Set,
                //  Three+ players playing thieving hangman against each other with their phrases, live, multiple winners
                [ThreePlus, AlternatingPuzzleSetter, Thieving] as Set,
                //  Three+ players playing regular hangman against each other with their phrases, live, single winners
                [ThreePlus, AlternatingPuzzleSetter, SingleWinner] as Set,
                //  Three+ players playing thieving hangman against each other with their phrases, live, single winners
                [ThreePlus, AlternatingPuzzleSetter, SingleWinner, Thieving] as Set,
                //  Three+ players playing regular hangman against each other with their phrases, turns, single winners
                [ThreePlus, AlternatingPuzzleSetter, SingleWinner, TurnBased] as Set,
                //  Three+ players playing thieving hangman against each other with their phrases, turns, single winners
                [ThreePlus, AlternatingPuzzleSetter, SingleWinner, Thieving, TurnBased] as Set,

                //  Multiple Players Using System Puzzles
                //  Three+ players playing regular hangman against system phrase, live, multiple winners
                [ThreePlus, SystemPuzzles] as Set,
                //  Three+ players playing regular hangman against system phrase, turns, multiple winners
                [ThreePlus, SystemPuzzles, TurnBased] as Set,
                //  Three+ players playing thieving hangman against system phrase, live, multiple winners
                [ThreePlus, SystemPuzzles, Thieving] as Set,
                //  Three+ players playing thieving hangman against system phrase, turns, multiple winners
                [ThreePlus, SystemPuzzles, Thieving, TurnBased] as Set,
                //  Three+ players playing regular hangman against system phrase, live, first to solve wins
                [ThreePlus, SystemPuzzles, SingleWinner] as Set,
                //  Three+ players playing thieving hangman against system phrase, live, first to solve wins
                [ThreePlus, SystemPuzzles, SingleWinner, Thieving] as Set,
                //  Three+ players playing thieving hangman against system phrase, turns, first to solve wins
                [ThreePlus, SystemPuzzles, SingleWinner, Thieving, TurnBased] as Set,
        ] as Set
    }
}