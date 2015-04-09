package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.game.handlers.AbstractPlayerRotatingGameActionHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler
import com.jtbdevelopment.TwistedHangman.game.handlers.StealLetterHandler
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame
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
        ] as AbstractPlayerRotatingGameActionHandler
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
        ] as AbstractPlayerRotatingGameActionHandler
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
        ] as AbstractPlayerRotatingGameActionHandler
        assert result.is(services.guessLetter(guess))
    }
}
