package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.junit.Test

/**
 * Date: 11/6/14
 * Time: 8:40 PM
 */
class ValidFeatureSetGameValidatorTest extends GroovyTestCase {
    ValidFeatureSetGameValidator validator = new ValidFeatureSetGameValidator()

    @Test
    void testErrorMessage() {
        assert "System Error - Combination of features is not valid somehow." == validator.errorMessage()
    }

    @Test
    void testValidAreValid() {
        GameFeature.ALLOWED_COMBINATIONS.each {
            Set<GameFeature> it ->
                Game game = new Game();
                game.features = it
                assert validator.validateGame(game)
        }
    }


    @Test
    void testValidAreValidWithNonValidatedField() {
        List<GameFeature> nonValidated = GameFeature.values().findAll { GameFeature it -> !it.validate }.toList()
        GameFeature.ALLOWED_COMBINATIONS.each {
            Set<GameFeature> it ->
                Game game = new Game();
                game.features = it
                game.features += nonValidated[0]
                nonValidated.add(nonValidated.remove(0))
                assert validator.validateGame(game)
        }
    }

    /*
    @Test
    void testInvalidAreInvalid() {
        List<Set<GameFeature>> featureCombos = []
        List<GameFeature> features = GameFeature.values().toList()
        println features
        features.permutations().each {  }
    }
    */

}
