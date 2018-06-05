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
import org.junit.Assert
import org.junit.Test

import java.time.Instant

/**
 * Date: 11/14/14
 * Time: 9:15 PM
 */
class GameMaskerTest extends TwistedHangmanTestCase {
    private GameMasker masker = new GameMasker()

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

        Assert.assertEquals([(PONE.md5): PONE.displayName], maskedGame.players)
        Assert.assertEquals([(PONE.md5): PONE.imageUrl], maskedGame.playerImages)
        Assert.assertEquals([(PONE.md5): PONE.profileUrl], maskedGame.playerProfiles)
        Assert.assertEquals(PONE.md5, maskedGame.initiatingPlayer)
        Assert.assertEquals([(PONE.md5): PlayerState.Accepted], maskedGame.playerStates)
        Assert.assertEquals([(PONE.md5): 5], maskedGame.playerRunningScores)
        Assert.assertEquals([(PONE.md5): 0], maskedGame.playerRoundScores)
        Assert.assertEquals(PONE.id.toHexString(), maskedGame.maskedForPlayerID)
        Assert.assertEquals(PONE.md5, maskedGame.maskedForPlayerMD5)
        Assert.assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5, maskedGame.wordPhraseSetter)
        Assert.assertEquals(game.featureData, maskedGame.featureData)

        Assert.assertEquals(1, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PONE.md5))
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        Assert.assertEquals([(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals("", maskedState.wordPhrase)
        checkUnmaskedData(maskedState, state)

        //  Flip game over and check word phrase
        game.gamePhase = GamePhase.RoundOver
        maskedGame = masker.maskGameForPlayer(game, PONE)
        maskedState = maskedGame.solverStates[PONE.md5]
        Assert.assertEquals(state.wordPhraseString, maskedState.wordPhrase)
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

        Assert.assertEquals([(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName], maskedGame.players)
        Assert.assertEquals([(PONE.md5): PONE.imageUrl, (PTWO.md5): PTWO.imageUrl], maskedGame.playerImages)
        Assert.assertEquals([(PONE.md5): PONE.profileUrl, (PTWO.md5): PTWO.profileUrl], maskedGame.playerProfiles)
        Assert.assertEquals(PTWO.md5, maskedGame.initiatingPlayer)
        Assert.assertEquals([(PONE.md5): PlayerState.Accepted, (PTWO.md5): PlayerState.Rejected], maskedGame.playerStates)
        Assert.assertEquals([(PONE.md5): 5, (PTWO.md5): 7], maskedGame.playerRunningScores)
        Assert.assertEquals([(PONE.md5): 1, (PTWO.md5): 0], maskedGame.playerRoundScores)
        Assert.assertEquals(PONE.id.toHexString(), maskedGame.maskedForPlayerID)
        Assert.assertEquals(PONE.md5, maskedGame.maskedForPlayerMD5)
        Assert.assertEquals(null, maskedGame.wordPhraseSetter)
        Assert.assertEquals([(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5], maskedGame.featureData)

        Assert.assertEquals(2, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PONE.md5))
        Assert.assertTrue(maskedGame.solverStates.containsKey(PTWO.md5))

        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        Assert.assertEquals([(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals("", maskedState.wordPhrase)

        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedData(maskedState, state2)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state2.wordPhraseString, maskedState.wordPhrase)
        Assert.assertEquals(state2.badlyGuessedLetters, maskedState.badlyGuessedLetters)
        Assert.assertEquals(state2.guessedLetters, maskedState.guessedLetters)

        //  Flip over
        game.gamePhase = GamePhase.NextRoundStarted
        maskedGame = masker.maskGameForPlayer(game, PONE)

        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        Assert.assertEquals([(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state1.wordPhraseString, maskedState.wordPhrase)

        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedData(maskedState, state2)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state2.wordPhraseString, maskedState.wordPhrase)
        Assert.assertEquals(state2.badlyGuessedLetters, maskedState.badlyGuessedLetters)
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
        Assert.assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5, maskedGame.wordPhraseSetter)
        Assert.assertEquals(PONE.id.toHexString(), maskedGame.maskedForPlayerID)
        Assert.assertEquals(PONE.md5, maskedGame.maskedForPlayerMD5)
        Assert.assertEquals(3, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PONE.md5))
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        Assert.assertEquals([(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals("", maskedState.wordPhrase)
        Assert.assertEquals(state1.badlyGuessedLetters, maskedState.badlyGuessedLetters)
        Assert.assertEquals(state1.guessedLetters, maskedState.guessedLetters)
        maskedState = maskedGame.solverStates[PTWO.md5]
        checkPartialMaskedData(maskedState, state2)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkPartialMaskedData(maskedState, state3)

        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        Assert.assertEquals(TwistedHangmanSystemPlayerCreator.TH_PLAYER.md5, maskedGame.wordPhraseSetter)
        Assert.assertEquals(PTHREE.md5, maskedGame.maskedForPlayerMD5)
        Assert.assertEquals(3, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PTHREE.md5))
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals("", maskedState.wordPhrase)
        maskedState = maskedGame.solverStates[PTWO.md5]
        checkPartialMaskedData(maskedState, state2)

        maskedState = maskedGame.solverStates[PONE.md5]
        checkPartialMaskedData(maskedState, state1)


        game.gamePhase = GamePhase.NextRoundStarted
        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        Assert.assertEquals(3, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PTHREE.md5))
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state3.wordPhraseString, maskedState.wordPhrase)
        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedData(maskedState, state2)
        Assert.assertEquals(state2.wordPhraseString, maskedState.wordPhrase)
        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        Assert.assertEquals(state1.wordPhraseString, maskedState.wordPhrase)
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
        Assert.assertEquals(PTWO.id.toHexString(), maskedGame.maskedForPlayerID)
        Assert.assertEquals(PTWO.md5, maskedGame.maskedForPlayerMD5)
        Assert.assertEquals(2, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PONE.md5))
        Assert.assertTrue(maskedGame.solverStates.containsKey(PTHREE.md5))
        Assert.assertEquals(PTWO.md5, maskedGame.wordPhraseSetter)
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        Assert.assertEquals([(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state1.wordPhraseString, maskedState.wordPhrase)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state3.wordPhraseString, maskedState.wordPhrase)


        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        Assert.assertEquals(PTHREE.md5, maskedGame.maskedForPlayerMD5)
        Assert.assertEquals(2, maskedGame.solverStates.size())
        Assert.assertTrue(maskedGame.solverStates.containsKey(PTHREE.md5))
        Assert.assertEquals(PTWO.md5, maskedGame.wordPhraseSetter)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals("", maskedState.wordPhrase)

        maskedState = maskedGame.solverStates[PONE.md5]
        checkPartialMaskedData(maskedState, state1)

        game.gamePhase = GamePhase.RoundOver
        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedData(maskedState, state3)
        Assert.assertEquals([(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state3.wordPhraseString, maskedState.wordPhrase)

        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedData(maskedState, state1)
        Assert.assertEquals([(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]], maskedState.featureData)
        Assert.assertEquals(state1.wordPhraseString, maskedState.wordPhrase)
        Assert.assertEquals(state1.badlyGuessedLetters, maskedState.badlyGuessedLetters)
        Assert.assertEquals(state1.guessedLetters, maskedState.guessedLetters)
    }

    private void checkUnmaskedData(MaskedIndividualGameState maskedState, IndividualGameState state) {
        Assert.assertEquals(state.blanksRemaining, maskedState.blanksRemaining)
        Assert.assertEquals(state.badlyGuessedLetters, maskedState.badlyGuessedLetters)
        Assert.assertEquals(state.category, maskedState.category)
        Assert.assertEquals(state.features, maskedState.features)
        Assert.assertEquals(state.guessedLetters, maskedState.guessedLetters)
        Assert.assertEquals(state.penalties, maskedState.penalties)
        Assert.assertEquals(state.penaltiesRemaining, maskedState.penaltiesRemaining)
        Assert.assertEquals(state.maxPenalties, maskedState.maxPenalties)
        Assert.assertEquals(state.isPuzzleOver(), maskedState.isPuzzleOver)
        Assert.assertEquals(state.isPuzzleSolved(), maskedState.isPuzzleSolved)
        Assert.assertEquals(state.isPlayerHung(), maskedState.isPlayerHung)
    }

    private void checkPartialMaskedData(MaskedIndividualGameState maskedState, IndividualGameState state) {
        Assert.assertEquals(state.blanksRemaining, maskedState.blanksRemaining)
        Assert.assertEquals(state.category, maskedState.category)
        Assert.assertEquals(state.features, maskedState.features)
        Assert.assertEquals(state.penalties, maskedState.penalties)
        Assert.assertEquals(state.penaltiesRemaining, maskedState.penaltiesRemaining)
        Assert.assertEquals(state.maxPenalties, maskedState.maxPenalties)
        Assert.assertEquals(state.isPuzzleOver(), maskedState.isPuzzleOver)
        Assert.assertEquals(maskedState.isPuzzleSolved, state.isPuzzleSolved())
        Assert.assertEquals(state.isPlayerHung(), maskedState.isPlayerHung)
        Assert.assertTrue(maskedState.featureData.isEmpty())
        Assert.assertEquals("", maskedState.wordPhrase)
        Assert.assertTrue(maskedState.badlyGuessedLetters.isEmpty())
        Assert.assertTrue(maskedState.guessedLetters.isEmpty())
    }

    private void checkUnmaskedGameFields(MaskedGame maskedGame, Game game) {
        Assert.assertEquals(game.id.toHexString(), maskedGame.id)
        Assert.assertEquals((game.completedTimestamp ? game.completedTimestamp.toEpochMilli() : null), maskedGame.completedTimestamp)
        Assert.assertEquals((game.created ? game.created.toEpochMilli() : null), maskedGame.created)
        Assert.assertEquals((game.declinedTimestamp ? game.declinedTimestamp.toEpochMilli() : null), maskedGame.declinedTimestamp)
        Assert.assertEquals((game.lastUpdate ? game.lastUpdate.toEpochMilli() : null), maskedGame.lastUpdate)
        Assert.assertEquals(game.features, maskedGame.features)
    }

    private void checkMultiPlayerGame(MaskedGame maskedGame) {
        Assert.assertEquals([(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName, (PTHREE.md5): PTHREE.displayName], maskedGame.players)
        Assert.assertEquals([(PONE.md5): PONE.imageUrl, (PTWO.md5): PTWO.imageUrl, (PTHREE.md5): PTHREE.imageUrl], maskedGame.playerImages)
        Assert.assertEquals([(PONE.md5): PONE.profileUrl, (PTWO.md5): PTWO.profileUrl, (PTHREE.md5): PTHREE.profileUrl], maskedGame.playerProfiles)

        Assert.assertEquals(PTWO.md5, maskedGame.initiatingPlayer)
        Assert.assertEquals([(PONE.md5): PlayerState.Accepted, (PTWO.md5): PlayerState.Rejected, (PTHREE.md5): PlayerState.Pending], maskedGame.playerStates)
        Assert.assertEquals([(PONE.md5): 5, (PTWO.md5): 7, (PTHREE.md5): -10], maskedGame.playerRunningScores)
        Assert.assertEquals([(PONE.md5): 1, (PTWO.md5): 0, (PTHREE.md5): -1], maskedGame.playerRoundScores)
        Assert.assertEquals([(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5], maskedGame.featureData)
    }

    private Game makeMultiPlayerGame(MongoPlayer puzzler, LinkedHashMap<ObjectId, IndividualGameState> states) {
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
