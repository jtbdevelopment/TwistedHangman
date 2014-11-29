package com.jtbdevelopment.TwistedHangman.game.state.masked

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.TwistedHangman.game.state.*
import com.jtbdevelopment.TwistedHangman.players.Player

import java.time.ZonedDateTime

/**
 * Date: 11/14/14
 * Time: 9:15 PM
 */
class GameMaskerTest extends TwistedHangmanTestCase {
    GameMasker masker = new GameMasker()

    public void testMaskingSinglePlayerGame() {
        IndividualGameState state = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
                penalties: 2,
                workingWordPhraseString: "__'_",
                wordPhraseString: "SAY'S",
        )
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                players: [PONE],
                wordPhraseSetter: Player.SYSTEM_PLAYER.id,
                created: ZonedDateTime.now(),
                completed: ZonedDateTime.now(),
                declined: ZonedDateTime.now(),
                featureData: [(GameFeature.DrawFace): ""],
                features: [GameFeature.SystemPuzzles, GameFeature.SinglePlayer],
                id: "1234",
                initiatingPlayer: PONE.id,
                lastUpdate: ZonedDateTime.now(),
                playerStates: [(PONE.id): PlayerState.Accepted],
                playerRunningScores: [(PONE.id): 5],
                playerRoundScores: [(PONE.id): 0],
                previousId: "34",
                rematched: ZonedDateTime.now(),
                round: new Random().nextInt(1000),
                solverStates: [(PONE.id): state],
                version: 10,
        )

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)

        assert maskedGame.players == [(PONE.md5): PONE.displayName]
        assert maskedGame.initiatingPlayer == PONE.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerState.Accepted]
        assert maskedGame.playerRunningScores == [(PONE.md5): 5]
        assert maskedGame.playerRoundScores == [(PONE.md5): 0]
        assert maskedGame.maskedForPlayerID == PONE.id
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.wordPhraseSetter == Player.SYSTEM_PLAYER.md5
        assert maskedGame.featureData == game.featureData

        assert maskedGame.solverStates.size() == 1 && maskedGame.solverStates.containsKey(PONE.md5)
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        assert maskedState.wordPhrase == "";
        checkUnmaskedData(maskedState, state)

        //  Flip game over and check word phrase
        game.gamePhase = GamePhase.Rematch
        maskedGame = masker.maskGameForPlayer(game, PONE)
        maskedState = maskedGame.solverStates[PONE.md5]
        assert maskedState.wordPhrase == state.wordPhraseString
    }

    public void testMaskingTwoPlayerHeadToHead() {
        IndividualGameState state1 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat1",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
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
                workingWordPhraseString: "__",
                wordPhraseString: "SAY",
        )
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                players: [PONE, PTWO],
                wordPhraseSetter: null,
                created: ZonedDateTime.now(),
                completed: ZonedDateTime.now(),
                declined: ZonedDateTime.now(),
                featureData: [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.id],
                features: [GameFeature.SystemPuzzles, GameFeature.SinglePlayer],
                id: "1234",
                initiatingPlayer: PTWO.id,
                lastUpdate: ZonedDateTime.now(),
                playerStates: [(PONE.id): PlayerState.Accepted, (PTWO.id): PlayerState.Rejected],
                playerRunningScores: [(PONE.id): 5, (PTWO.id): 7],
                playerRoundScores: [(PONE.id): 1, (PTWO.id): 0],
                previousId: "34",
                rematched: ZonedDateTime.now(),
                solverStates: [(PONE.id): state1, (PTWO.id): state2],
                round: new Random().nextInt(1000),
                version: 10,
        )

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)

        assert maskedGame.players == [(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName]
        assert maskedGame.initiatingPlayer == PTWO.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerState.Accepted, (PTWO.md5): PlayerState.Rejected]
        assert maskedGame.playerRunningScores == [(PONE.md5): 5, (PTWO.md5): 7]
        assert maskedGame.playerRoundScores == [(PONE.md5): 1, (PTWO.md5): 0]
        assert maskedGame.maskedForPlayerID == PONE.id
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
        game.gamePhase = GamePhase.Rematched
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

    public void testMaskingMultiPlayerSystemPuzzler() {
        IndividualGameState state1 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat1",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
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

        LinkedHashMap<String, IndividualGameState> states = [(PONE.id): state1, (PTWO.id): state2, (PTHREE.id): state3]
        Game game = makeMultiPlayerGame(Player.SYSTEM_PLAYER, states)

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.wordPhraseSetter == Player.SYSTEM_PLAYER.md5
        assert maskedGame.maskedForPlayerID == PONE.id
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
        assert maskedGame.wordPhraseSetter == Player.SYSTEM_PLAYER.md5
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


        game.gamePhase = GamePhase.Rematched
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

    public void testMaskingMultiPlayerNonSystemPuzzler() {
        IndividualGameState state1 = new IndividualGameState(
                badlyGuessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                category: "cat1",
                featureData: [(GameFeature.DrawGallows): PONE.id, (GameFeature.Thieving): [true, true, false]],
                features: [GameFeature.TurnBased],
                guessedLetters: [(char) 'A', (char) 'B'] as SortedSet,
                maxPenalties: 10,
                moveCount: 2,
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
                penalties: 3,
                workingWordPhraseString: "__",
                wordPhraseString: "SAY",
        )

        LinkedHashMap<String, IndividualGameState> states = [(PONE.id): state1, (PTHREE.id): state3]
        Game game = makeMultiPlayerGame(PTWO, states)

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PTWO)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.maskedForPlayerID == PTWO.id
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

        game.gamePhase = GamePhase.Rematch
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
        assert maskedState.badlyGuessedLetters == state.badlyGuessedLetters
        assert maskedState.category == state.category
        assert maskedState.features == state.features
        assert maskedState.guessedLetters == state.guessedLetters
        assert maskedState.penalties == state.penalties
        assert maskedState.penaltiesRemaining == state.penaltiesRemaining
        assert maskedState.maxPenalties == state.maxPenalties
        assert maskedState.isGameOver == state.isGameOver()
        assert maskedState.isGameWon == state.isGameWon()
        assert maskedState.isGameLost == state.isGameLost()
    }

    protected void checkPartialMaskedData(MaskedIndividualGameState maskedState, IndividualGameState state) {
        assert maskedState.category == state.category
        assert maskedState.features == state.features
        assert maskedState.penalties == state.penalties
        assert maskedState.penaltiesRemaining == state.penaltiesRemaining
        assert maskedState.maxPenalties == state.maxPenalties
        assert maskedState.isGameOver == state.isGameOver()
        assert maskedState.isGameWon == state.isGameWon()
        assert maskedState.isGameLost == state.isGameLost()
        assert maskedState.featureData.isEmpty()
        assert maskedState.wordPhrase == ""
        assert maskedState.badlyGuessedLetters.isEmpty()
        assert maskedState.guessedLetters.isEmpty()
    }

    protected void checkUnmaskedGameFields(MaskedGame maskedGame, Game game) {
        assert maskedGame.id == game.id
        assert maskedGame.gamePhase == game.gamePhase
        assert maskedGame.completed == (game.completed ? game.completed.toEpochSecond() : null)
        assert maskedGame.created == (game.created ? game.created.toEpochSecond() : null)
        assert maskedGame.declined == (game.declined ? game.declined.toEpochSecond() : null)
        assert maskedGame.lastUpdate == (game.lastUpdate ? game.lastUpdate.toEpochSecond() : null)
        assert maskedGame.rematched == (game.rematched ? game.rematched.toEpochSecond() : null)
        assert maskedGame.features == game.features
        assert maskedGame.round == game.round
    }

    protected void checkMultiPlayerGame(MaskedGame maskedGame) {
        assert maskedGame.players == [(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName, (PTHREE.md5): PTHREE.displayName]
        assert maskedGame.initiatingPlayer == PTWO.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerState.Accepted, (PTWO.md5): PlayerState.Rejected, (PTHREE.md5): PlayerState.Pending]
        assert maskedGame.playerRunningScores == [(PONE.md5): 5, (PTWO.md5): 7, (PTHREE.md5): -10]
        assert maskedGame.playerRoundScores == [(PONE.md5): 1, (PTWO.md5): 0, (PTHREE.md5): -1]
        assert maskedGame.featureData == [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5]
    }

    protected Game makeMultiPlayerGame(Player puzzler, LinkedHashMap<String, IndividualGameState> states) {
        Game game = new Game(
                gamePhase: GamePhase.Playing,
                players: [PONE, PTWO, PTHREE],
                wordPhraseSetter: puzzler.id,
                created: ZonedDateTime.now(),
                completed: ZonedDateTime.now(),
                declined: ZonedDateTime.now(),
                featureData: [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.id],
                features: [GameFeature.SystemPuzzles, GameFeature.SinglePlayer],
                id: "1234",
                initiatingPlayer: PTWO.id,
                lastUpdate: ZonedDateTime.now(),
                playerStates: [(PONE.id): PlayerState.Accepted, (PTWO.id): PlayerState.Rejected, (PTHREE.id): PlayerState.Pending],
                playerRunningScores: [(PONE.id): 5, (PTWO.id): 7, (PTHREE.id): -10],
                playerRoundScores: [(PONE.id): 1, (PTWO.id): 0, (PTHREE.id): -1],
                previousId: "34",
                rematched: ZonedDateTime.now(),
                solverStates: states,
                round: new Random().nextInt(1000),
                version: 10,
        )
        game
    }

}
