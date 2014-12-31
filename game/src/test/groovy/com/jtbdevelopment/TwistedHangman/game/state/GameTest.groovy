package com.jtbdevelopment.TwistedHangman.game.state

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.games.games.PlayerState
import org.bson.types.ObjectId

import java.time.ZonedDateTime

/**
 * Date: 11/9/14
 * Time: 3:50 PM
 */
class GameTest extends TwistedHangmanTestCase {

    //  TODO - move to base
    void testEquals() {
        Game one = makeSimpleGame("1")
        Game oneClone = one.clone()
        Game two = makeSimpleGame("2")
        assert one.equals(oneClone)
        assert oneClone.equals(one)
        assertFalse one.equals(two)
        assertFalse one.equals(134L)
        assertFalse one.equals(null)
    }

    //  TODO - move to base
    void testHashCode() {
        ObjectId SOMEID = new ObjectId()
        Game game = new Game(id: SOMEID)
        assert SOMEID.toHexString().hashCode() == game.hashCode()
    }


    void testToString() {
        assert new Game(
                id: new ObjectId("0a123".padRight(24, "0")),
                version: 23,
                created: ZonedDateTime.parse("2014-05-30T10:15:30+01:00[Europe/Paris]"),
                completedTimestamp: ZonedDateTime.parse("2014-05-31T10:15:30+01:00[Europe/Paris]"),
                declinedTimestamp: ZonedDateTime.parse("2013-05-31T10:15:30+01:00[Europe/Paris]"),
                gamePhase: GamePhase.Playing,
                features: [GameFeature.SingleWinner],
                initiatingPlayer: PONE.id,
                lastUpdate: ZonedDateTime.parse("2013-01-31T10:15:30+01:00[Europe/Paris]"),
                players: [PONE, PTWO],
                playerRunningScores: [(PONE.id): 5],
                playerStates: [(PONE.id): PlayerState.Accepted],
                rematchTimestamp: ZonedDateTime.parse("2013-05-29T10:15:30+01:00[Europe/Paris]"),
                solverStates: [(PONE.id): new IndividualGameState()],
                wordPhraseSetter: PTWO.id,
                previousId: new ObjectId("033".padRight(24, "0")),
                featureData: [(GameFeature.DrawGallows): "y"]).toString() == "Game{id='0a1230000000000000000000', version=23, created=2014-05-30T10:15:30+02:00[Europe/Paris], declinedTimestamp=2013-05-31T10:15:30+02:00[Europe/Paris], lastUpdate=2013-01-31T10:15:30+01:00[Europe/Paris], completedTimestamp=2014-05-31T10:15:30+02:00[Europe/Paris], rematchTimestamp=2013-05-29T10:15:30+02:00[Europe/Paris], previousId='033000000000000000000000', gamePhase=Playing, initiatingPlayer=100000000000000000000000, players=[Player{id='100000000000000000000000', source='MADEUP', sourceId='MADEUP1', displayName='1', disabled=false}, Player{id='200000000000000000000000', source='MADEUP', sourceId='MADEUP2', displayName='2', disabled=false}], playerStates=[100000000000000000000000:Accepted], features=[SingleWinner], featureData=[DrawGallows:y], wordPhraseSetter=200000000000000000000000, solverStates=[100000000000000000000000:IndividualGameState{category='', wordPhrase=[], workingWordPhrase=[], maxPenalties=6, moveCount=0, penalties=0, features=[], badlyGuessedLetters=[], guessedLetters=[], featureData=[:], blanksRemaining=0}], playerRunningScores=[100000000000000000000000:5]}"
    }
}
