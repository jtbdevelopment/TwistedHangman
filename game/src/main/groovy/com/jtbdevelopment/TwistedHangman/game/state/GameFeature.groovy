package com.jtbdevelopment.TwistedHangman.game.state

import groovy.transform.CompileStatic

/**
 * Date: 11/3/14
 * Time: 6:45 AM
 */
@CompileStatic
enum GameFeature {
    SinglePlayer("Single Player", true, true, true),
    TwoPlayer("2 Players", true, true, true),
    ThreePlus("3+ Players", true, true, true),

    Thieving("For a penalty, steal a letter from the puzzle.", false, false),
    ThievingCountTracking("Stolen Letter Count.", true, false),
    ThievingPositionTracking("Stolen Letter Markers.", true, false),
    ThievingLetters("Set of letters similar to guesses", true, false),

    Live("Each player can solves their puzzle independently of the others until the game is over."),
    TurnBased("Guessing and stealing is turn-based instead of live."),

    SystemPuzzles("Twisted Hangman provide the puzzles."),
    AlternatingPuzzleSetter("Take turns setting the puzzle for other players.  The player does not play that round but can see the other players progress."),
    Head2Head("Each player creates a puzzle for the other."),

    AllComplete("Round ends when all players solve their puzzle or hang."),
    SingleWinner("Only one player can win.  Round finishes when first person wins, or all players lose.  Unfinished puzzles are counted as draws."),


    DrawGallows("Draw the gallows too, giving three extra chances.", false, false),
    DrawFace("Draw the face too, giving four extra chances.", false, false)

    final boolean broadCategory
    final boolean internal
    final boolean validate
    final String description


    GameFeature(
            final String description, boolean internal = false, validate = true, broadCategory = false) {
        this.internal = internal
        this.description = description
        this.validate = validate
        this.broadCategory = broadCategory
    }

    static final Set<Set<GameFeature>> ALLOWED_COMBINATIONS
    static {
        ALLOWED_COMBINATIONS = [
                //  Single Player
                [SinglePlayer, SystemPuzzles, SingleWinner, Live] as Set,

                //  TWO PLAYER ONLY
                //  Two players playing regular hangman against each other with their phrases, live, multiple winners
                [TwoPlayer, Head2Head, AllComplete, Live] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
                [TwoPlayer, Head2Head, AllComplete, TurnBased] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, single winners
                [TwoPlayer, Head2Head, SingleWinner, Live] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, single winners
                [TwoPlayer, Head2Head, SingleWinner, TurnBased] as Set,
                //  Two players playing regular hangman against system phrase, live, multiple winners
                [TwoPlayer, SystemPuzzles, AllComplete, Live] as Set,
                //  Two players playing regular hangman against system phrase, turns, multiple winners
                [TwoPlayer, SystemPuzzles, AllComplete, TurnBased] as Set,
                //  Two players playing regular hangman against system phrase, live, first to solve wins
                [TwoPlayer, SystemPuzzles, SingleWinner, Live] as Set,
                //  Two players playing hangman against system phrase, turns, first to solve wins
                [TwoPlayer, SystemPuzzles, SingleWinner, TurnBased] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, multiple winners
                [TwoPlayer, AlternatingPuzzleSetter, AllComplete, Live] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
                [TwoPlayer, AlternatingPuzzleSetter, AllComplete, TurnBased] as Set,
                //  Two players playing regular hangman against each other with their phrases, live, single winners
                [TwoPlayer, AlternatingPuzzleSetter, SingleWinner, Live] as Set,
                //  Two players playing regular hangman against each other with their phrases, turns, single winners
                [TwoPlayer, AlternatingPuzzleSetter, SingleWinner, TurnBased] as Set,

                //  Multiple Players Not Using System ID - take turns setting phrases
                //  Three+ players playing regular hangman against each other with their phrases, live, multiple winners
                [ThreePlus, AlternatingPuzzleSetter, AllComplete, Live] as Set,
                //  Three+ players playing regular hangman against each other with their phrases, turns, multiple winners
                [ThreePlus, AlternatingPuzzleSetter, AllComplete, TurnBased] as Set,
                //  Three+ players playing regular hangman against each other with their phrases, live, single winners
                [ThreePlus, AlternatingPuzzleSetter, SingleWinner, Live] as Set,
                //  Three+ players playing regular hangman against each other with their phrases, turns, single winners
                [ThreePlus, AlternatingPuzzleSetter, SingleWinner, TurnBased] as Set,

                //  Multiple Players Using System Puzzles
                //  Three+ players playing regular hangman against system phrase, live, multiple winners
                [ThreePlus, SystemPuzzles, AllComplete, Live] as Set,
                //  Three+ players playing regular hangman against system phrase, turns, multiple winners
                [ThreePlus, SystemPuzzles, AllComplete, TurnBased] as Set,
                //  Three+ players playing regular hangman against system phrase, live, first to solve wins
                [ThreePlus, SystemPuzzles, SingleWinner, Live] as Set,
                //  Three+ players playing regular hangman against system phrase, live, first to solve wins
                [ThreePlus, SystemPuzzles, SingleWinner, TurnBased] as Set,
        ] as Set
    }
}