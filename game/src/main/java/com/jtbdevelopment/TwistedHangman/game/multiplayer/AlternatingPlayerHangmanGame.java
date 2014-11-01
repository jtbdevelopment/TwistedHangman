package com.jtbdevelopment.TwistedHangman.game.multiplayer;

import com.jtbdevelopment.TwistedHangman.game.Player;
import com.jtbdevelopment.TwistedHangman.game.mechanics.HangmanGame;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: 10/29/14
 * Time: 7:18 PM
 * <p>
 * At start of game, each player submits a word phrase for others which are each a round
 * During each round, the submitter observes other players solving puzzle in god mode seeing all details
 * Other solvers can see general state of other players but not their letters or current display state
 * Game is complete when all rounds are played.
 * <p>
 * Each player gains a point for solving, loses a point for hanging
 * If only a single winner per round, other solvers receive no point for not being first,
 * otherwise
 */
public interface AlternatingPlayerHangmanGame extends PlayerHangmanGame {
    public boolean isSingleWinnerPerRound();

    public Player getCurrentChallenger();

    public Set<Player> getCurrentSolvers();

    public Map<Player, HangmanGame> getCurrentRound();

    public List<Map<Player, HangmanGame>> getPreviousRounds();

    public List<Map<Player, HangmanGame>> getRemainingRounds();
}
