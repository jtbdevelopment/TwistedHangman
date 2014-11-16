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
                playerStates: [(PONE.id): PlayerChallengeState.Accepted],
                playerScores: [(PONE.id): 5],
                previousId: "34",
                rematched: ZonedDateTime.now(),
                solverStates: [(PONE.id): state],
                version: 10,
        )

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)

        assert maskedGame.players == [(PONE.md5): PONE.displayName]
        assert maskedGame.initiatingPlayer == PONE.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerChallengeState.Accepted]
        assert maskedGame.playerScores == [(PONE.md5): 5]
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.maskedForPlayer == PONE
        assert maskedGame.wordPhraseSetter == Player.SYSTEM_PLAYER.md5
        assert maskedGame.featureData == game.featureData

        assert maskedGame.solverStates.size() == 1 && maskedGame.solverStates.containsKey(PONE.md5)

        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        checkUnmaskedState(maskedState, state)
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
                gamePhase: GamePhase.Rematch,
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
                playerStates: [(PONE.id): PlayerChallengeState.Accepted, (PTWO.id): PlayerChallengeState.Rejected],
                playerScores: [(PONE.id): 5, (PTWO.id): 7],
                previousId: "34",
                rematched: ZonedDateTime.now(),
                solverStates: [(PONE.id): state1, (PTWO.id): state2],
                version: 10,
        )

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)

        assert maskedGame.players == [(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName]
        assert maskedGame.initiatingPlayer == PTWO.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerChallengeState.Accepted, (PTWO.md5): PlayerChallengeState.Rejected]
        assert maskedGame.playerScores == [(PONE.md5): 5, (PTWO.md5): 7]
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.maskedForPlayer == PONE
        assert maskedGame.wordPhraseSetter == null
        assert maskedGame.featureData == [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5]

        assert maskedGame.solverStates.size() == 2 && maskedGame.solverStates.containsKey(PONE.md5) && maskedGame.solverStates.containsKey(PTWO.md5)

        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedState(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]

        maskedState = maskedGame.solverStates[PTWO.md5]
        checkUnmaskedState(maskedState, state2)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
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

        Player puzzler = Player.SYSTEM_PLAYER
        LinkedHashMap<String, IndividualGameState> states = [(PONE.id): state1, (PTWO.id): state2, (PTHREE.id): state3]
        Game game = makeMultiPlayerGame(puzzler, states)

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PONE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.wordPhraseSetter == Player.SYSTEM_PLAYER.md5
        assert maskedGame.maskedForPlayerMD5 == PONE.md5
        assert maskedGame.maskedForPlayer == PONE
        assert maskedGame.solverStates.size() == 1 && maskedGame.solverStates.containsKey(PONE.md5)
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedState(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]


        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.wordPhraseSetter == Player.SYSTEM_PLAYER.md5
        assert maskedGame.maskedForPlayerMD5 == PTHREE.md5
        assert maskedGame.maskedForPlayer == PTHREE
        assert maskedGame.solverStates.size() == 1 && maskedGame.solverStates.containsKey(PTHREE.md5)
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedState(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
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

        Player puzzler = PTWO
        LinkedHashMap<String, IndividualGameState> states = [(PONE.id): state1, (PTHREE.id): state3]
        Game game = makeMultiPlayerGame(puzzler, states)

        MaskedGame maskedGame = masker.maskGameForPlayer(game, PTWO)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.maskedForPlayerMD5 == PTWO.md5
        assert maskedGame.maskedForPlayer == PTWO
        assert maskedGame.solverStates.size() == 2 && maskedGame.solverStates.containsKey(PONE.md5) && maskedGame.solverStates.containsKey(PTHREE.md5)
        assert maskedGame.wordPhraseSetter == PTWO.md5
        MaskedIndividualGameState maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedState(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]
        maskedState = maskedGame.solverStates[PONE.md5]
        checkUnmaskedState(maskedState, state1)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PONE.md5, (GameFeature.Thieving): [true, true, false]]


        maskedGame = masker.maskGameForPlayer(game, PTHREE)
        checkUnmaskedGameFields(maskedGame, game)
        checkMultiPlayerGame(maskedGame)
        assert maskedGame.maskedForPlayerMD5 == PTHREE.md5
        assert maskedGame.maskedForPlayer == PTHREE
        assert maskedGame.solverStates.size() == 1 && maskedGame.solverStates.containsKey(PTHREE.md5)
        assert maskedGame.wordPhraseSetter == PTWO.md5
        maskedState = maskedGame.solverStates[PTHREE.md5]
        checkUnmaskedState(maskedState, state3)
        assert maskedState.featureData == [(GameFeature.DrawGallows): PTWO.md5, (GameFeature.Thieving): [true, true, false]]
    }

    protected void checkUnmaskedState(MaskedIndividualGameState maskedState, IndividualGameState state) {
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

    protected void checkUnmaskedGameFields(MaskedGame maskedGame, Game game) {
        assert maskedGame.id == game.id
        assert maskedGame.gamePhase == game.gamePhase
        assert maskedGame.completed == game.completed
        assert maskedGame.created == game.created
        assert maskedGame.declined == game.declined
        assert maskedGame.lastUpdate == game.lastUpdate
        assert maskedGame.features == game.features
        assert maskedGame.rematched == game.rematched
    }

    protected void checkMultiPlayerGame(MaskedGame maskedGame) {
        assert maskedGame.players == [(PONE.md5): PONE.displayName, (PTWO.md5): PTWO.displayName, (PTHREE.md5): PTHREE.displayName]
        assert maskedGame.initiatingPlayer == PTWO.md5
        assert maskedGame.playerStates == [(PONE.md5): PlayerChallengeState.Accepted, (PTWO.md5): PlayerChallengeState.Rejected, (PTHREE.md5): PlayerChallengeState.Pending]
        assert maskedGame.playerScores == [(PONE.md5): 5, (PTWO.md5): 7, (PTHREE.md5): -10]
        assert maskedGame.featureData == [(GameFeature.DrawFace): "", (GameFeature.SingleWinner): PTWO.md5]
    }

    protected Game makeMultiPlayerGame(Player puzzler, LinkedHashMap<String, IndividualGameState> states) {
        Game game = new Game(
                gamePhase: GamePhase.Rematch,
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
                playerStates: [(PONE.id): PlayerChallengeState.Accepted, (PTWO.id): PlayerChallengeState.Rejected, (PTHREE.id): PlayerChallengeState.Pending],
                playerScores: [(PONE.id): 5, (PTWO.id): 7, (PTHREE.id): -10],
                previousId: "34",
                rematched: ZonedDateTime.now(),
                solverStates: states,
                version: 10,
        )
        game
    }

}
