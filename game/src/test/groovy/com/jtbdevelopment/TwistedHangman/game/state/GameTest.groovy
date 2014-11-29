package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase

import java.time.ZonedDateTime

/**
 * Date: 11/9/14
 * Time: 3:50 PM
 */
class GameTest extends TwistedHangmanTestCase {

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


    void testHashCode() {
        String SOMEID = "SOMEID"
        Game game = new Game(id: SOMEID)
        assert SOMEID.hashCode() == game.hashCode()
    }


    void testToString() {
        assert new Game(
                id: "0x123",
                version: 23,
                created: ZonedDateTime.parse("2014-05-30T10:15:30+01:00[Europe/Paris]"),
                completed: ZonedDateTime.parse("2014-05-31T10:15:30+01:00[Europe/Paris]"),
                declined: ZonedDateTime.parse("2013-05-31T10:15:30+01:00[Europe/Paris]"),
                gamePhase: GamePhase.Playing,
                features: [GameFeature.SingleWinner],
                initiatingPlayer: PONE.id,
                lastUpdate: ZonedDateTime.parse("2013-01-31T10:15:30+01:00[Europe/Paris]"),
                players: [PONE, PTWO],
                playerRunningScores: [(PONE.id): 5],
                playerStates: [(PONE.id): PlayerState.Accepted],
                rematched: ZonedDateTime.parse("2013-05-29T10:15:30+01:00[Europe/Paris]"),
                solverStates: [(PONE.id): new IndividualGameState()],
                wordPhraseSetter: PTWO.id,
                previousId: "033",
                featureData: [(GameFeature.DrawGallows): "y"]).toString() == "Game{id='0x123', version=23, created=2014-05-30T10:15:30+02:00[Europe/Paris], declined=2013-05-31T10:15:30+02:00[Europe/Paris], lastUpdate=2013-01-31T10:15:30+01:00[Europe/Paris], completed=2014-05-31T10:15:30+02:00[Europe/Paris], rematched=2013-05-29T10:15:30+02:00[Europe/Paris], previousId='033', gamePhase=Playing, initiatingPlayer=1, players=[Player{id='1', source='MADEUP', displayName='1', disabled=false}, Player{id='2', source='MADEUP', displayName='2', disabled=false}], playerStates=[1:Accepted], features=[SingleWinner], featureData=[DrawGallows:y], wordPhraseSetter=2, solverStates=[1:IndividualGameState{category='', wordPhrase=[], workingWordPhrase=[], maxPenalties=6, moveCount=0, penalties=0, features=[], badlyGuessedLetters=[], guessedLetters=[], featureData=[:]}], playerRunningScores=[1:5]}"
    }
}
