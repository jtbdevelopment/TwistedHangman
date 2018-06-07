package com.jtbdevelopment.TwistedHangman.game.state;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/3/14 Time: 6:45 AM
 */
public enum GameFeature {
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
  AlternatingPuzzleSetter(
      "Take turns setting the puzzle for other players.  The player does not play that round but can see the other players progress."),
  Head2Head("Each player creates a puzzle for the other."),
  AllComplete("Round ends when all players solve their puzzle or hang."),
  SingleWinner(
      "Only one player can win.  Round finishes when first person wins, or all players lose.  Unfinished puzzles are counted as draws."),
  DrawGallows("Draw the gallows too, giving three extra chances.", false, false),
  DrawFace("Draw the face too, giving four extra chances.", false, false);

  private static final Set<Set<GameFeature>> ALLOWED_COMBINATIONS;

  static {
    ALLOWED_COMBINATIONS = new HashSet<>();
    //  Single Player
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(SinglePlayer, SystemPuzzles, SingleWinner, Live)));
    //  TWO PLAYER ONLY
    //  Two players playing regular hangman against each other with their phrases, live, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, Head2Head, AllComplete, Live)));
    //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, Head2Head, AllComplete, TurnBased)));
    //  Two players playing regular hangman against each other with their phrases, live, single winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, Head2Head, SingleWinner, Live)));
    //  Two players playing regular hangman against each other with their phrases, turns, single winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, Head2Head, SingleWinner, TurnBased)));
    //  Two players playing regular hangman against system phrase, live, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, SystemPuzzles, AllComplete, Live)));
    //  Two players playing regular hangman against system phrase, turns, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, SystemPuzzles, AllComplete, TurnBased)));
    //  Two players playing regular hangman against system phrase, live, first to solve wins
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, SystemPuzzles, SingleWinner, Live)));
    //  Two players playing hangman against system phrase, turns, first to solve wins
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, SystemPuzzles, SingleWinner, TurnBased)));
    //  Two players playing regular hangman against each other with their phrases, live, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, AlternatingPuzzleSetter, AllComplete, Live)));
    //  Two players playing regular hangman against each other with their phrases, turns, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(
            Arrays.asList(TwoPlayer, AlternatingPuzzleSetter, AllComplete, TurnBased)));
    //  Two players playing regular hangman against each other with their phrases, live, single winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(TwoPlayer, AlternatingPuzzleSetter, SingleWinner, Live)));
    //  Two players playing regular hangman against each other with their phrases, turns, single winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(
            Arrays.asList(TwoPlayer, AlternatingPuzzleSetter, SingleWinner, TurnBased)));
    //  Multiple Players Not Using System ID - take turns setting phrases
    //  Three+ players playing regular hangman against each other with their phrases, live, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(ThreePlus, AlternatingPuzzleSetter, AllComplete, Live)));
    //  Three+ players playing regular hangman against each other with their phrases, turns, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(
            Arrays.asList(ThreePlus, AlternatingPuzzleSetter, AllComplete, TurnBased)));
    //  Three+ players playing regular hangman against each other with their phrases, live, single winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(ThreePlus, AlternatingPuzzleSetter, SingleWinner, Live)));
    //  Three+ players playing regular hangman against each other with their phrases, turns, single winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(
            Arrays.asList(ThreePlus, AlternatingPuzzleSetter, SingleWinner, TurnBased)));
    //  Multiple Players Using System Puzzles
    //  Three+ players playing regular hangman against system phrase, live, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(ThreePlus, SystemPuzzles, AllComplete, Live)));
    //  Three+ players playing regular hangman against system phrase, turns, multiple winners
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(ThreePlus, SystemPuzzles, AllComplete, TurnBased)));
    //  Three+ players playing regular hangman against system phrase, live, first to solve wins
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(ThreePlus, SystemPuzzles, SingleWinner, Live)));
    //  Three+ players playing regular hangman against system phrase, live, first to solve wins
    ALLOWED_COMBINATIONS
        .add(new HashSet<>(Arrays.asList(ThreePlus, SystemPuzzles, SingleWinner, TurnBased)));
  }

  private final boolean broadCategory;
  private final boolean internal;
  private final boolean validate;
  private final String description;

  GameFeature(final String description, boolean internal, boolean validate, boolean broadCategory) {
    this.internal = internal;
    this.description = description;
    this.validate = validate;
    this.broadCategory = broadCategory;
  }

  GameFeature(final String description, boolean internal, boolean validate) {
    this(description, internal, validate, false);
  }

  GameFeature(final String description) {
    this(description, false, true, false);
  }

  public static Set<Set<GameFeature>> getAllowedCombinations() {
    return ALLOWED_COMBINATIONS;
  }

  public final boolean getBroadCategory() {
    return broadCategory;
  }

  public final boolean isBroadCategory() {
    return broadCategory;
  }

  public final boolean getInternal() {
    return internal;
  }

  public final boolean isInternal() {
    return internal;
  }

  public final boolean getValidate() {
    return validate;
  }

  public final boolean isValidate() {
    return validate;
  }

  public final String getDescription() {
    return description;
  }
}
