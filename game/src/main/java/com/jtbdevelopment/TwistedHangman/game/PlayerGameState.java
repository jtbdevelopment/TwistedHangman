package com.jtbdevelopment.TwistedHangman.game;

/**
 * Date: 10/30/14
 * Time: 6:59 AM
 */
public interface PlayerGameState {
    public int getScore();

    public ChallengeState getChallengeState();

    public enum ChallengeState {
        Open,
        Accepted,
        Rejected
    }
}
