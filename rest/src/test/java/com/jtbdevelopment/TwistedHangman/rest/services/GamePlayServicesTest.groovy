package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.TwistedHangman.game.state.PlayerChallengeState
import com.jtbdevelopment.TwistedHangman.game.state.masked.MaskedGame
import groovy.transform.TypeChecked

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/15/2014
 * Time: 1:49 PM
 */
class GamePlayServicesTest extends GroovyTestCase {
    private static final String PID = "PID1"
    private static final String GID = "GID2"
    private final MaskedGame result = new MaskedGame()
    GamePlayServices services = new GamePlayServices()

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        services.playerID.set(PID)
        services.gameID.set(GID)
    }

    void testGet() {
        services.gameGetterHandler = [
                handleAction: {
                    String p, String g ->
                        assert p == PID
                        assert g == GID
                        result
                }
        ] as GameGetterHandler
        assert result.is(services.getGame())
    }

    void testGetAnnotations() {
        def m = GamePlayServices.getMethod("getGame", [] as Class[])
        assert (m.annotations.size() == 2 ||
                (m.annotations.size() == (3) && m.isAnnotationPresent(TypeChecked.TypeCheckingInfo.class))
        )
        assert m.isAnnotationPresent(GET.class)
        assert m.isAnnotationPresent(Produces.class)
        assert m.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assertFalse m.isAnnotationPresent(Path.class)
    }


    void testActionAnnotations() {

        Map<String, List<Object>> stuff = [
                //  method: [name, params, path, path param values, consumes
                "createRematch": ["rematch", [], [], []],
                "rejectGame"   : ["reject", [], [], []],
                "acceptGame"   : ["accept", [], [], []],
                "setPuzzle"    : ["puzzle", [SetPuzzleHandler.CategoryAndWordPhrase.class], [], [MediaType.APPLICATION_JSON]],
                "stealLetter"  : ["steal/{position}", [int.class], ["position"], []],
                "guessLetter"  : ["guess/{letter}", [String.class], ["letter"], []]
        ]
        stuff.each {
            String method, List<Object> details ->
                def m = GamePlayServices.getMethod(method, details[1] as Class[])
                int expectedA = 3 + details[3].size
                assert (m.annotations.size() == expectedA ||
                        (m.annotations.size() == (expectedA + 1) && m.isAnnotationPresent(TypeChecked.TypeCheckingInfo.class))
                )
                assert m.isAnnotationPresent(PUT.class)
                assert m.isAnnotationPresent(Produces.class)
                assert m.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
                assert m.isAnnotationPresent(Path.class)
                assert m.getAnnotation(Path.class).value() == details[0]
                if (details[3].size > 0) {
                    assert m.isAnnotationPresent(Consumes.class)
                    assert m.getAnnotation(Consumes.class).value() == details[3]
                }
                if (details[2].size > 0) {
                    int count = 0
                    details[2].each {
                        String pp ->
                            ((PathParam) m.parameterAnnotations[count][0]).value() == pp
                            ++count
                    }
                }
        }
    }

    void testCreateRematch() {
        services.rematchHandler = [
                handleAction: {
                    String p, String g ->
                        assert p == PID
                        assert g == GID
                        result
                }
        ] as ChallengeToRematchHandler
        assert result.is(services.createRematch())
    }

    void testRejectGame() {
        services.responseHandler = [
                handleAction: {
                    String p, String g, PlayerChallengeState r ->
                        assert p == PID
                        assert g == GID
                        assert r == PlayerChallengeState.Rejected
                        result
                }
        ] as ChallengeResponseHandler
        assert result.is(services.rejectGame())
    }

    void testAcceptGame() {
        services.responseHandler = [
                handleAction: {
                    String p, String g, PlayerChallengeState r ->
                        assert p == PID
                        assert g == GID
                        assert r == PlayerChallengeState.Accepted
                        result
                }
        ] as ChallengeResponseHandler
        assert result.is(services.acceptGame())
    }

    void testSetPuzzle() {
        String c = "Cat"
        String w = "Anim"
        services.puzzleHandler = [
                handleAction: {
                    String p, String g, SetPuzzleHandler.CategoryAndWordPhrase r ->
                        assert p == PID
                        assert g == GID
                        assert r.category == c
                        assert r.wordPhrase == w
                        result
                }
        ] as SetPuzzleHandler
        assert result.is(services.setPuzzle(new SetPuzzleHandler.CategoryAndWordPhrase(category: c, wordPhrase: w)))
    }

    void testStealLetter() {
        int pos = 4
        services.stealLetterHandler = [
                handleAction: {
                    String p, String g, int r ->
                        assert p == PID
                        assert g == GID
                        assert r == pos
                        result
                }
        ] as StealLetterHandler
        assert result.is(services.stealLetter(pos))
    }

    void testGuessLetter() {
        String guess = "G"
        services.guessLetterHandler = [
                handleAction: {
                    String p, String g, char r ->
                        assert p == PID
                        assert g == GID
                        assert r == 'G'
                        result
                }
        ] as GuessLetterHandler
        assert result.is(services.guessLetter(guess))
    }

    void testGuessLetterNull() {
        String guess = null
        services.guessLetterHandler = [
                handleAction: {
                    String p, String g, char r ->
                        assert p == PID
                        assert g == GID
                        assert r == ' '
                        result
                }
        ] as GuessLetterHandler
        assert result.is(services.guessLetter(guess))
    }

    void testGuessLetterEmpty() {
        String guess = ""
        services.guessLetterHandler = [
                handleAction: {
                    String p, String g, char r ->
                        assert p == PID
                        assert g == GID
                        assert r == ' '
                        result
                }
        ] as GuessLetterHandler
        assert result.is(services.guessLetter(guess))
    }
}