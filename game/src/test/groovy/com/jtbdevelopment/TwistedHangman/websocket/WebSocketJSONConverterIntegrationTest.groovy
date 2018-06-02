package com.jtbdevelopment.TwistedHangman.websocket

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedIndividualGameState
import com.jtbdevelopment.TwistedHangman.json.TwistedHangmanJacksonRegistration
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.core.spring.jackson.ObjectMapperFactory
import com.jtbdevelopment.games.mongo.json.MongoPlayerJacksonRegistration
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState
import com.jtbdevelopment.games.websocket.WebSocketJSONConverter
import com.jtbdevelopment.games.websocket.WebSocketMessage
import org.junit.Before
import org.junit.Test

/**
 * Date: 12/9/14
 * Time: 11:47 AM
 *
 */
class WebSocketJSONConverterIntegrationTest extends TwistedHangmanTestCase {
    private WebSocketJSONConverter webSocketJsonConverter = new WebSocketJSONConverter()

    MaskedGame maskedGame = new MaskedGame(
            completedTimestamp: 100,
            created: 1000,
            featureData: [(GameFeature.DrawFace): 'A String'],
            features: [GameFeature.DrawFace, GameFeature.TurnBased],
            gamePhase: GamePhase.Setup,
            id: 'XYZ',
            lastUpdate: 5000,
            maskedForPlayerID: PONE.id.toHexString(),
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
            wordPhraseSetter: TwistedHangmanSystemPlayerCreator.TH_MD5
    )

    String expectedString = "{\"messageType\":\"Game\",\"game\":{\"id\":\"XYZ\",\"previousId\":null,\"version\":null,\"round\":10,\"created\":1000,\"lastUpdate\":5000,\"completedTimestamp\":100,\"gamePhase\":\"Setup\",\"players\":{\"196c643ff2d27ff53cbd574c08c7726f\":\"100000000000000000000000\"},\"playerImages\":{},\"playerProfiles\":{},\"features\":[\"DrawFace\",\"TurnBased\"],\"featureData\":{\"DrawFace\":\"A String\"},\"maskedForPlayerID\":\"100000000000000000000000\",\"maskedForPlayerMD5\":\"196c643ff2d27ff53cbd574c08c7726f\",\"declinedTimestamp\":null,\"rematchTimestamp\":null,\"initiatingPlayer\":null,\"playerStates\":{\"196c643ff2d27ff53cbd574c08c7726f\":\"Accepted\"},\"wordPhraseSetter\":\"eb3e279a50b4c330f8d4a9e2abc678fe\",\"solverStates\":{\"196c643ff2d27ff53cbd574c08c7726f\":{\"wordPhrase\":\"555\",\"workingWordPhrase\":\"____\",\"badlyGuessedLetters\":[\"A\",\"B\"],\"guessedLetters\":[\"B\"],\"featureData\":{\"Thieving\":10,\"DrawGallows\":\"X\"},\"category\":\"Test\",\"maxPenalties\":13,\"moveCount\":4,\"penalties\":3,\"blanksRemaining\":10,\"features\":[\"AllComplete\"],\"isPuzzleSolved\":false,\"isPlayerHung\":true,\"isPuzzleOver\":false,\"penaltiesRemaining\":5}},\"playerRoundScores\":{\"196c643ff2d27ff53cbd574c08c7726f\":10},\"playerRunningScores\":{\"196c643ff2d27ff53cbd574c08c7726f\":13},\"allPlayers\":null},\"player\":null,\"message\":null}"
    @Before
    void setUp() throws Exception {
        ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory(
                Collections.emptyList(),
                Collections.emptyList(),
                [
                        new TwistedHangmanJacksonRegistration(),
                        new MongoPlayerJacksonRegistration()
                ]
        )
        webSocketJsonConverter.setMapper(objectMapperFactory.getObject())
    }

    @Test
    void testToJson() {
        def encoded = webSocketJsonConverter.encode(new WebSocketMessage(
                messageType:
                        WebSocketMessage.MessageType.Game,
                game: maskedGame))
        assert expectedString == encoded
    }

    @Test
    void testFromJson() {
        WebSocketMessage message = webSocketJsonConverter.decode(expectedString)
        assert WebSocketMessage.MessageType.Game == message.messageType
        assert message.game in MaskedGame
        //  Selected comparisons
        assert message.game.solverStates.size() == maskedGame.solverStates.size()
        assert message.game.solverStates.keySet() as Set == maskedGame.solverStates.keySet()
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
