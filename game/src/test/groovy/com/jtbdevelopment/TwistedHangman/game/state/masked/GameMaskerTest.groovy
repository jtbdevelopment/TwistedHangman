package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.TwistedHangman.game.state.IndividualGameState
import com.jtbdevelopment.TwistedHangman.game.state.masking.GameMasker
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedIndividualGameState
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.PlayerState
import org.bson.types.ObjectId
import org.junit.Test

import java.time.Instant

/**
 * Date: 11/14/14
 * Time: 9:15 PM
 */
class GameMaskerTest extends TwistedHangmanTestCase {
    GameMasker masker = new GameMasker()

    @Test
    void testMaskingSinglePlayerGame() {
        IndividualGameState state = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
                penalties: 2,
                blanksRemaining: 4,
                workingWordPhraseString: "__'_",
                wordPhraseString: "SAY'S",
        )
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                players: [PONE],
                wordPhraseSetter: (ObjectId) TwistedHangmanSystemPlayerCreator.TH_PLAYER.id,
                created: Instant.now(),
                completedTimestamp: Instant.now(),
                declinedTimestamp: Instant.now(),
                featureData: [(GameFeature.DrawFace): ""],
                features: [GameFeature.SystemPuzzles, GameFeature.SinglePlayer],
                id: new ObjectId("1234".padRight(24, "0")),
                initiatingPlayer: PONE.id,
                lastUpdate: Instant.now(),
                playerStates: [(PONE.id): PlayerState.Accepted],
                playerRunningScores: [(PONE.id): 5],
                playerRoundScores: [(PONE.id): 0],
                previousId: new ObjectId("34".padRight(24, "0")),
                rematchTimestamp: Instant.now(),
                round: new Random().nextInt(1000),
                solverStates: [(PONE.id): state],
                version: 10,
        )

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)

        assert maskedGame.players == [(PONE.md5): PONE.displayName]
        assert maskedGame.playerImages == [(PONE.md5): PONE.imageUrl]
        assert maskedGame.playerProfiles == [(PONE.md5): PONE.profileUrl]
        assert maskedGame.initiatingPlayer == PONE.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerState.Accepted]
        assert maskedGame.playerRunningScores == [(PONE.md5): 5]
        assert maskedGame.playerRoundScores == [(PONE.md5): 0]
        assert maskedGame.maskedForPlayerID == PONE.id.toHexString()
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.wordPhraseSetter == TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5
        assert maskedGame.featureData == game.featureData

        assert maskedGame.solverStates.size() == 1 && maskedGame.solverStates.containsKey(PONE.md5)
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == "";
        checkUnmaskedData(maskedState, state)

        //  Flip game over and check word phrase
        game.gamePhase = GamePhase.RoundOver
        maskedGame = masker.maskGameForPlayer(game, PONE)
        maskedState = maskedGame.solverStates[PONE.md5]
        assert maskedState.wordPhrase == state.wordPhraseString
    }

    @Test
    void testMaskingTwoPlayerHeadToHead() {
        IndividualGameState state1 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat1",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
                blanksRemaining: 4,
                penalties: 2,
                workingWordPhraseString: "__'_",
                wordPhraseString: "SAY'S",
        )
        IndividualGameState state2 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat2",
                featureData: [(GameFeature.DrawGallows): PTWO.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 4,
                penalties: 3,
                blanksRemaining: 3,
                workingWordPhraseString: "__",
                wordPhraseString: "SAY",
        )
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                players: [PONE, PTWO],
                wordPhraseSetter: null,
                created: Instant.now(),
                completedTimestamp: Instant.now(),
                declinedTimestamp: Instant.now(),
                featureData: [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.id],
                features: [GameFeature.SystemPuzzles, GameFeature.SinglePlayer],
                id: new ObjectId("1234".padRight(24, "0")),
                initiatingPlayer: PTWO.id,
                lastUpdate: Instant.now(),
                playerStates: [(PONE.id): PlayerState.Accepted, (PTWO.id): PlayerState.Rejected],
                playerRunningScores: [(PONE.id): 5, (PTWO.id): 7],
                playerRoundScores: [(PONE.id): 1, (PTWO.id): 0],
                previousId: new ObjectId("34".padRight(24, "0")),
                rematchTimestamp: Instant.now(),
                solverStates: [(PONE.id): state1, (PTWO.id): state2],
                round: new Random().nextInt(1000),
                version: 10,
        )

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)

        assert maskedGame.players == [(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName]
        assert maskedGame.playerImages == [(PONE.md5): PONE.imageUrl, (PTWO.md5): PTWO.imageUrl]
        assert maskedGame.playerProfiles == [(PONE.md5): PONE.profileUrl, (PTWO.md5): PTWO.profileUrl]
        assert maskedGame.initiatingPlayer == PTWO.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerState.Accepted, (PTWO.md5): PlayerState.Rejected]
        assert maskedGame.playerRunningScores == [(PONE.md5): 5, (PTWO.md5): 7]
        assert maskedGame.playerRoundScores == [(PONE.md5): 1, (PTWO.md5): 0]
        assert maskedGame.maskedForPlayerID == PONE.id.toHexString()
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.wordPhraseSetter == null
        assert maskedGame.featureData == [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5]

        assert maskedGame.solverStates.size() == 2 && maskedGame.solverStates.containsKey(PONE.md5) && maskedGame.solverStates.containsKey(PTWO.md5)

        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == ""

        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedData(maskedState, state2)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state2.wordPhraseString
        assert maskedState.badlyGuessedLetters == state2.badlyGuessedLetters
        assert maskedState.guessedLetters == state2.guessedLetters

        //  Flip over
        game.gamePhase = GamePhase.NextRoundStarted
        maskedGame = masker.maskGameForPlayer(game, PONE)

        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state1.wordPhraseString

        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedData(maskedState, state2)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state2.wordPhraseString
        assert maskedState.badlyGuessedLetters == state2.badlyGuessedLetters
        assert maskedState.guessedLetters == state2.guessedLetters
    }

    @Test
    void testMaskingMultiPlayerSystemPuzzler() {
        IndividualGameState state1 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat1",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
                blanksRemaining: 4,
                penalties: 2,
                workingWordPhraseString: "__'_",
                wordPhraseString: "SAY'S",
        )
        IndividualGameState state2 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat2",
                featureData: [(GameFeature.DrawGallows): PTWO.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 4,
                blanksRemaining: 3,
                penalties: 3,
                workingWordPhraseString: "__",
                wordPhraseString: "SAY",
        )
        IndividualGameState state3 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat3",
                featureData: [(GameFeature.DrawGallows): PTWO.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 4,
                penalties: 3,
                workingWordPhraseString: "__",
                wordPhraseString: "SAY",
        )

        LinkedHashMap<ObjectId, IndividualGameState> states = [(PONE.id): state1, (PTWO.id): state2, (PTHREE.id): state3]
        Game game = makeMultiPlayerGame(TwistedHangmanSystemPlayerCreator.TH_PLAYER, states)

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.wordPhraseSetter == TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5
        assert maskedGame.maskedForPlayerID == PONE.id.toHexString()
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.solverStates.size() == 3 && maskedGame.solverStates.containsKey(PONE.md5)
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == ""
        assert maskedState.badlyGuessedLetters == state1.badlyGuessedLetters
        assert maskedState.guessedLetters == state1.guessedLetters
        maskedState = maskedGame.solverStates[PTWO.md5]
        checkPartialMaskedData(maskedState, state2)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkPartialMaskedData(maskedState, state3)

        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.wordPhraseSetter == TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5
        assert maskedGame.maskedForPlayerMD5 == PTHREE.md5
        assert maskedGame.solverStates.size() == 3 && maskedGame.solverStates.containsKey(PTHREE.md5)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == ""
        maskedState = maskedGame.solverStates[PTWO.md5]
        checkPartialMaskedData(maskedState, state2)

        maskedState = maskedGame.solverStates[PONE.md5]
        checkPartialMaskedData(maskedState, state1)


        game.gamePhase = GamePhase.NextRoundStarted
        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        assert maskedGame.solverStates.size() == 3 && maskedGame.solverStates.containsKey(PTHREE.md5)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state3.wordPhraseString
        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedData(maskedState, state2)
        assert maskedState.wordPhrase == state2.wordPhraseString
        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        assert maskedState.wordPhrase == state1.wordPhraseString
    }

    @Test
    void testMaskingMultiPlayerNonSystemPuzzler() {
        IndividualGameState state1 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat1",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
                blanksRemaining: 4,
                penalties: 2,
                workingWordPhraseString: "__'_",
                wordPhraseString: "SAY'S",
        )
        IndividualGameState state3 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat3",
                featureData: [(GameFeature.DrawGallows): PTWO.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 4,
                blanksRemaining: 3,
                penalties: 3,
                workingWordPhraseString: "__",
                wordPhraseString: "SAY",
        )

        LinkedHashMap<ObjectId, IndividualGameState> states = [(PONE.id): state1, (PTHREE.id): state3]
        Game game = makeMultiPlayerGame(PTWO, states)

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PTWO)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.maskedForPlayerID == PTWO.id.toHexString()
        assert maskedGame.maskedForPlayerMD5 == PTWO.md5
        assert maskedGame.solverStates.size() == 2 && maskedGame.solverStates.containsKey(PONE.md5) && maskedGame.solverStates.containsKey(PTHREE.md5)
        assert maskedGame.wordPhraseSetter == PTWO.md5
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state1.wordPhraseString
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state3.wordPhraseString


        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.maskedForPlayerMD5 == PTHREE.md5
        assert maskedGame.solverStates.size() == 2 && maskedGame.solverStates.containsKey(PTHREE.md5)
        assert maskedGame.wordPhraseSetter == PTWO.md5
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == ""

        maskedState = maskedGame.solverStates[PONE.md5]
        checkPartialMaskedData(maskedState, state1)

        game.gamePhase = GamePhase.RoundOver
        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state3.wordPhraseString

        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == state1.wordPhraseString
        assert maskedState.badlyGuessedLetters == state1.badlyGuessedLetters
        assert maskedState.guessedLetters == state1.guessedLetters
    }

    protected void checkUnmaskedData(MaskedIndividualGameState maskedState, IndividualGameState state) {
        assert maskedState.blanksRemaining == state.blanksRemaining
        assert maskedState.badlyGuessedLetters == state.badlyGuessedLetters
        assert maskedState.category == state.category
        assert maskedState.features == state.features
        assert maskedState.guessedLetters == state.guessedLetters
        assert maskedState.penalties == state.penalties
        assert maskedState.penaltiesRemaining == state.penaltiesRemaining
        assert maskedState.maxPenalties == state.maxPenalties
        assert maskedState.isPuzzleOver == state.isPuzzleOver()
        assert maskedState.isPuzzleSolved == state.isPuzzleSolved()
        assert maskedState.isPlayerHung == state.isPlayerHung()
    }

    protected void checkPartialMaskedData(MaskedIndividualGameState maskedState, IndividualGameState state) {
        assert maskedState.blanksRemaining == state.blanksRemaining
        assert maskedState.category == state.category
        assert maskedState.features == state.features
        assert maskedState.penalties == state.penalties
        assert maskedState.penaltiesRemaining == state.penaltiesRemaining
        assert maskedState.maxPenalties == state.maxPenalties
        assert maskedState.isPuzzleOver == state.isPuzzleOver()
        assert maskedState.isPuzzleSolved == state.isPuzzleSolved()
        assert maskedState.isPlayerHung == state.isPlayerHung()
        assert maskedState.featureData.isEmpty()
        assert maskedState.wordPhrase == ""
        assert maskedState.badlyGuessedLetters.isEmpty()
        assert maskedState.guessedLetters.isEmpty()
    }

    protected void checkUnmaskedGameFields(MaskedGame maskedGame, Game game) {
        assert maskedGame.id == game.id.toHexString()
        assert maskedGame.completedTimestamp == (game.completedTimestamp ? game.completedTimestamp.toEpochMilli() : null)
        assert maskedGame.created == (game.created ? game.created.toEpochMilli() : null)
        assert maskedGame.declinedTimestamp == (game.declinedTimestamp ? game.declinedTimestamp.toEpochMilli() : null)
        assert maskedGame.lastUpdate == (game.lastUpdate ? game.lastUpdate.toEpochMilli() : null)
        assert maskedGame.features == game.features
    }

    protected void checkMultiPlayerGame(MaskedGame maskedGame) {
        assert maskedGame.players == [(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName, (PTHREE.md5): PTHREE.displayName]
        assert maskedGame.playerImages == [(PONE.md5): PONE.imageUrl, (PTWO.md5): PTWO.imageUrl, (PTHREE.md5): PTHREE.imageUrl]
        assert maskedGame.playerProfiles == [(PONE.md5): PONE.profileUrl, (PTWO.md5): PTWO.profileUrl, (PTHREE.md5): PTHREE.profileUrl]

        assert maskedGame.initiatingPlayer == PTWO.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerState.Accepted, (PTWO.md5): PlayerState.Rejected, (PTHREE.md5): PlayerState.Pending]
        assert maskedGame.playerRunningScores == [(PONE.md5): 5, (PTWO.md5): 7, (PTHREE.md5): -10]
        assert maskedGame.playerRoundScores == [(PONE.md5): 1, (PTWO.md5): 0, (PTHREE.md5): -1]
        assert maskedGame.featureData == [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5]
    }

    protected Game makeMultiPlayerGame(MongoPlayer puzzler, LinkedHashMap<ObjectId, IndividualGameState> states) {
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                players: [PONE, PTWO, PTHREE],
                wordPhraseSetter: puzzler.id,
                created: Instant.now(),
                completedTimestamp: Instant.now(),
                declinedTimestamp: Instant.now(),
                featureData: [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.id],
                features: [GameFeature.SystemPuzzles, GameFeature.SinglePlayer],
                id: new ObjectId("1234".padRight(24, "0")),
                initiatingPlayer: PTWO.id,
                lastUpdate: Instant.now(),
                playerStates: [(PONE.id): PlayerState.Accepted, (PTWO.id): PlayerState.Rejected, (PTHREE.id): PlayerState.Pending],
                playerRunningScores: [(PONE.id): 5, (PTWO.id): 7, (PTHREE.id): -10],
                playerRoundScores: [(PONE.id): 1, (PTWO.id): 0, (PTHREE.id): -1],
                previousId: new ObjectId("34".padRight(24, "0")),
                rematchTimestamp: Instant.now(),
                solverStates: states,
                round: new Random().nextInt(1000),
                version: 10,
        )
        game
    }
}
