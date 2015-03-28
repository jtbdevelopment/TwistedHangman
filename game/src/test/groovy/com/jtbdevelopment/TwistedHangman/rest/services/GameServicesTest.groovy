package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.*
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
import com.jtbdevelopment.games.state.PlayerState
import groovy.transform.TypeChecked
import org.bson.types.ObjectId

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Date: 11/15/2014
 * Time: 1:49 PM
 */
class GameServicesTest extends GroovyTestCase {
    private static final ObjectId PID = new ObjectId()
    private static final ObjectId GID = new ObjectId()
    private final MaskedGame result = new MaskedGame()
    GameServices services = new GameServices()

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        services.playerID.set(PID)
        services.gameID.set(GID)
    }

    void testActionAnnotations() {

        Map<String, List<Object>> stuff = [
                //  method: [name, params, path, path param values, consumes
                "createRematch": ["rematch", [], [], []],
                "rejectGame"   : ["reject", [], [], []],
                "quitGame": ["quit", [], [], []],
                "acceptGame"   : ["accept", [], [], []],
                "setPuzzle"    : ["puzzle", [SetPuzzleHandler.CategoryAndWordPhrase.class], [], [MediaType.APPLICATION_JSON]],
                "stealLetter"  : ["steal/{position}", [int.class], ["position"], []],
                "guessLetter"  : ["guess/{letter}", [String.class], ["letter"], []]
        ]
        stuff.each {
            String method, List<Object> details ->
                def m = GameServices.getMethod(method, details[1] as Class[])
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
                    ObjectId p, ObjectId g ->
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
                    ObjectId p, ObjectId g, PlayerState r ->
                        assert p == PID
                        assert g == GID
                        assert r == PlayerState.Rejected
                        result
                }
        ] as ChallengeResponseHandler
        assert result.is(services.rejectGame())
    }

    void testAcceptGame() {
        services.responseHandler = [
                handleAction: {
                    ObjectId p, ObjectId g, PlayerState r ->
                        assert p == PID
                        assert g == GID
                        assert r == PlayerState.Accepted
                        result
                }
        ] as ChallengeResponseHandler
        assert result.is(services.acceptGame())
    }

    void testQuitGame() {
        services.quitHandler = [
                handleAction: {
                    ObjectId p, ObjectId g ->
                        assert p == PID
                        assert g == GID
                        result
                }
        ] as QuitHandler
        assert result.is(services.quitGame())
    }

    void testSetPuzzle() {
        String c = "Cat"
        String w = "Anim"
        services.puzzleHandler = [
                handleAction: {
                    ObjectId p, ObjectId g, SetPuzzleHandler.CategoryAndWordPhrase r ->
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
                    ObjectId p, ObjectId g, int r ->
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
                    ObjectId p, ObjectId g, char r ->
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
                    ObjectId p, ObjectId g, char r ->
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
                    ObjectId p, ObjectId g, char r ->
                        assert p == PID
                        assert g == GID
                        assert r == ' '
                        result
                }
        ] as GuessLetterHandler
        assert result.is(services.guessLetter(guess))
    }
}
