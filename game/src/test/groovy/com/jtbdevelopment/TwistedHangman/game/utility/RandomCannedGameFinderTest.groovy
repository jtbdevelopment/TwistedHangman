package com.jtbdevelopment.TwistedHangman.game.utility

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.util.StringUtils

/**
 * Date: 11/2/14
 * Time: 4:01 PM
 */
@ContextConfiguration("/spring-context-game-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//  TODO - despring and mock
class RandomCannedGameFinderTest extends GroovyTestCase {
    @Autowired
    RandomCannedGameFinder randomCannedGame

    @Test
    public void testGeneral() {
        CannedGame game = randomCannedGame.getRandomGame()
        assert game != null
        assertFalse StringUtils.isEmpty(game.id)
        assertFalse StringUtils.isEmpty(game.wordPhrase)
        assertFalse StringUtils.isEmpty(game.category)
    }

    @Test
    public void testSpecificSource() {
        CannedGame game = randomCannedGame.getRandomGame("WOFS")
        assert game != null
        assertFalse StringUtils.isEmpty(game.id)
        assertFalse StringUtils.isEmpty(game.wordPhrase)
        assertFalse StringUtils.isEmpty(game.category)
    }

    @Test(expected = IllegalStateException.class)
    public void testNonexistent() {
        randomCannedGame.getRandomGame("NEVEREXISTS")
        fail("Exception expected")
    }
}
