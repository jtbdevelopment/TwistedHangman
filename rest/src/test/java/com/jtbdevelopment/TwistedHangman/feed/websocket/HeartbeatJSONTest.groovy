package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import com.jtbdevelopment.TwistedHangman.game.state.PlayerState
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedIndividualGameState
import com.jtbdevelopment.TwistedHangman.players.Player

/**
 * Date: 12/9/14
 * Time: 11:47 AM
 */
class HeartbeatJSONTest extends TwistedHangmanTestCase {
    HeartbeatJSON heartbeatJSON = new HeartbeatJSON()

    MaskedGame maskedGame = new MaskedGame(
            completed: 100,
            created: 1000,
            featureData: [(GameFeature.DrawFace): 'A String'],
            features: [GameFeature.DrawFace, GameFeature.TurnBased],
            gamePhase: GamePhase.Setup,
            id: 'XYZ',
            lastUpdate: 5000,
            maskedForPlayerID: PONE.id,
            maskedForPlayerMD5: PONE.md5,
            players: [(PONE.md5): PONE.displayName],
            playerRoundScores: [(PONE.md5): 10],
            playerRunningScores: [(PONE.md5): 13],
            playerStates: [(PONE.md5): PlayerState.Accepted],
            round: 10,
            solverStates: [(PONE.md5): new MaskedIndividualGameState(
                    badlyGuessedLetters: [new Character((char) 'A'), new Character((char) 'B')] as SortedSet,
                    blanksRemaining: 10,
                    category: 'Test',
                    features: [GameFeature.AllComplete],
                    featureData: [(GameFeature.Thieving): 10, (GameFeature.DrawGallows): 'X'],
                    guessedLetters: [new Character((char) 'B')] as SortedSet,
                    maxPenalties: 13,
                    moveCount: 4,
                    isPuzzleOver: false,
                    isPuzzleSolved: false,
                    isPlayerHung: true,
                    penalties: 3,
                    penaltiesRemaining: 5,
                    workingWordPhrase: '____',
                    wordPhrase: '555',
            )],
            wordPhraseSetter: Player.SYSTEM_ID_MD5
    )

    String expectedString = "{\"messageType\":\"Game\",\"game\":{\"maskedForPlayerID\":\"1\",\"maskedForPlayerMD5\":\"0c244ca528410c6a625410af71982ba9\",\"id\":\"XYZ\",\"created\":1000,\"lastUpdate\":5000,\"declined\":null,\"completed\":100,\"rematched\":null,\"round\":10,\"gamePhase\":\"Setup\",\"initiatingPlayer\":null,\"players\":{\"0c244ca528410c6a625410af71982ba9\":\"1\"},\"playerStates\":{\"0c244ca528410c6a625410af71982ba9\":\"Accepted\"},\"features\":[\"DrawFace\",\"TurnBased\"],\"featureData\":{\"DrawFace\":\"A String\"},\"wordPhraseSetter\":\"03df6771c3b3bce06b58551bc8885dec\",\"solverStates\":{\"0c244ca528410c6a625410af71982ba9\":{\"wordPhrase\":\"555\",\"workingWordPhrase\":\"____\",\"badlyGuessedLetters\":[\"A\",\"B\"],\"guessedLetters\":[\"B\"],\"featureData\":{\"Thieving\":10,\"DrawGallows\":\"X\"},\"category\":\"Test\",\"maxPenalties\":13,\"moveCount\":4,\"penalties\":3,\"blanksRemaining\":10,\"features\":[\"AllComplete\"],\"isPuzzleSolved\":false,\"isPlayerHung\":true,\"isPuzzleOver\":false,\"penaltiesRemaining\":5}},\"playerRoundScores\":{\"0c244ca528410c6a625410af71982ba9\":10},\"playerRunningScores\":{\"0c244ca528410c6a625410af71982ba9\":13}},\"message\":null}"

    void testToJson() {
        assert expectedString == heartbeatJSON.encode(new TWMessage(
                messageType:
                        TWMessage.MessageType.Game,
                game: maskedGame))
    }

    void testFromJson() {
        TWMessage message = heartbeatJSON.decode(expectedString)
        assert TWMessage.MessageType.Game == message.messageType
        assert message.game in MaskedGame
        //  Selected comparisons
        assert message.game.solverStates.size() == maskedGame.solverStates.size()
        assert message.game.solverStates.keySet() == maskedGame.solverStates.keySet()
        assert message.game.solverStates[PONE.md5].featureData == maskedGame.solverStates[PONE.md5].featureData
        assert message.game.solverStates[PONE.md5].features == maskedGame.solverStates[PONE.md5].features
        assert message.game.solverStates[PONE.md5].badlyGuessedLetters == maskedGame.solverStates[PONE.md5].badlyGuessedLetters
        assert message.game.features == maskedGame.features
        assert message.game.featureData == maskedGame.featureData
        assert message.game.maskedForPlayerMD5 == maskedGame.maskedForPlayerMD5
        assert message.game.playerStates == maskedGame.playerStates
        assert message.game.playerRunningScores == maskedGame.playerRunningScores
        assert message.game.playerRoundScores == maskedGame.playerRoundScores

    }
}