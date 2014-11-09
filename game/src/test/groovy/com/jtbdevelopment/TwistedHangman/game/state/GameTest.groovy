package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.THGroovyTestCase
import com.jtbdevelopment.TwistedHangman.players.Player
import org.junit.Test

import java.time.ZonedDateTime

/**
 * Date: 11/9/14
 * Time: 3:50 PM
 */
class GameTest extends THGroovyTestCase {
    @Test
    void testEquals() {
        Game one = new Game(id: "1")
        Game oneClone = one.clone()
        Game two = new Game(id: "2")
        assert one.equals(oneClone)
        assert oneClone.equals(one)
        assertFalse one.equals(two)
        assertFalse one.equals(134L)
        assertFalse one.equals(null)
    }

    @Test
    void testHashCode() {
        String SOMEID = "SOMEID"
        Player player = new Player(id: SOMEID)
        assert SOMEID.hashCode() == player.hashCode()
    }

    @Test
    void testToString() {
        assert new Game(
                id: "0x123",
                version: 23,
                created: ZonedDateTime.parse("2014-05-30T10:15:30+01:00[Europe/Paris]"),
                completed: ZonedDateTime.parse("2014-05-31T10:15:30+01:00[Europe/Paris]"),
                declined: ZonedDateTime.parse("2013-05-31T10:15:30+01:00[Europe/Paris]"),
                gamePhase: Game.GamePhase.Playing,
                features: [GameFeature.SingleWinner, GameFeature.DrawGallows],
                initiatingPlayer: PONE,
                lastMove: ZonedDateTime.parse("2013-01-31T10:15:30+01:00[Europe/Paris]"),
                players: [PONE, PTWO],
                playerScores: [(PONE): 5, (PTWO): 10],
                playerStates: [(PONE): Game.PlayerChallengeState.Accepted, (PTWO): Game.PlayerChallengeState.Rejected],
                rematched: ZonedDateTime.parse("2013-05-29T10:15:30+01:00[Europe/Paris]"),
                solverStates: [(PONE): new IndividualGameState()],
                wordPhraseSetter: PTWO,
                previousId: "033",
                featureData: [(GameFeature.DrawGallows): "y", (GameFeature.SinglePlayer): 1],
                rematchId: "345").toString() == "Game{id='0x123', version=23, created=2014-05-30T10:15:30+02:00[Europe/Paris], declined=2013-05-31T10:15:30+02:00[Europe/Paris], lastMove=2013-01-31T10:15:30+01:00[Europe/Paris], completed=2014-05-31T10:15:30+02:00[Europe/Paris], rematched=2013-05-29T10:15:30+02:00[Europe/Paris], previousId='033', rematchId='345', gamePhase=Playing, initiatingPlayer=Player{id='1', source='MADEUP', displayName='1', disabled=false}, players=[Player{id='1', source='MADEUP', displayName='1', disabled=false}, Player{id='2', source='MADEUP', displayName='2', disabled=false}], playerStates=[Player{id='1', source='MADEUP', displayName='1', disabled=false}:Accepted, Player{id='2', source='MADEUP', displayName='2', disabled=false}:Rejected], features=[DrawGallows, SingleWinner], featureData=[DrawGallows:y, SinglePlayer:1], wordPhraseSetter=Player{id='2', source='MADEUP', displayName='2', disabled=false}, solverStates=[Player{id='1', source='MADEUP', displayName='1', disabled=false}:com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState@a4102b8], playerScores=[Player{id='1', source='MADEUP', displayName='1', disabled=false}:5, Player{id='2', source='MADEUP', displayName='2', disabled=false}:10]}"
    }
}
