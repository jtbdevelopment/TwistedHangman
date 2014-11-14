package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.dao.CannedGameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.system.RandomCannedGameFinderException
import org.junit.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

/**
 * Date: 11/2/14
 * Time: 4:01 PM
 */
class RandomCannedGameFinderTest extends GroovyTestCase {
    RandomCannedGameFinder finder = new RandomCannedGameFinder()

    @Test
    public void testReturnsARandomGameFromSingleItemUniverse() {
        CannedGame game = [] as CannedGame
        Page<CannedGame> page = [getContent: { [game] }] as Page<CannedGame>
        finder.repository = [
                count  : {
                    1L
                },
                findAll: {
                    PageRequest it ->
                        assert it.pageNumber == 0
                        assert it.pageSize == 1
                        page
                }
        ] as CannedGameRepository
        assert game.is(finder.getRandomGame())
    }

    @Test
    public void testExceptionIfNoGames() {
        finder.repository = [
                count: {
                    0L
                }
        ] as CannedGameRepository
        try {
            finder.getRandomGame()
        } catch (RandomCannedGameFinderException e) {
            assert e.message == "No pre-made games were found."
        }
    }

    @Test
    public void testExceptionIfNoContent() {
        Page<CannedGame> page = [getContent: { [] }] as Page<CannedGame>
        finder.repository = [
                count  : {
                    1L
                },
                findAll: {
                    PageRequest it ->
                        assert it.pageNumber == 0
                        assert it.pageSize == 1
                        page
                }
        ] as CannedGameRepository
        try {
            finder.getRandomGame()
        } catch (RandomCannedGameFinderException e) {
            assert e.message == "Pre-made game failed to load."
        }
    }

    @Test
    public void testExceptionIfTooMuchContent() {
        CannedGame game = [] as CannedGame
        Page<CannedGame> page = [getContent: { [game, game] }] as Page<CannedGame>
        finder.repository = [
                count  : {
                    1L
                },
                findAll: {
                    PageRequest it ->
                        assert it.pageNumber == 0
                        assert it.pageSize == 1
                        page
                }
        ] as CannedGameRepository
        try {
            finder.getRandomGame()
        } catch (RandomCannedGameFinderException e) {
            assert e.message == "Pre-made game failed to load."
        }
    }

    @Test
    public void testReturnsARandomGameFromLargeUniverse() {
        CannedGame game = [] as CannedGame
        long top = Long.MAX_VALUE / 2
        Page<CannedGame> page = [getContent: { [game] }] as Page<CannedGame>
        finder.repository = [
                count  : {
                    top
                },
                findAll: {
                    PageRequest it ->
                        assert it.pageNumber < Integer.MAX_VALUE
                        assert it.pageSize == 1
                        page
                }
        ] as CannedGameRepository
        assert game.is(finder.getRandomGame())
    }

    @Test
    public void testReturnsARandomGameFromWithSource() {
        String source = "Interwebs"
        CannedGame game = [] as CannedGame
        long top = Integer.MAX_VALUE / 2
        finder.repository = [
                countBySource: {
                    String it ->
                        assert it == source
                        top
                },
                findBySource : {
                    String s, PageRequest p ->
                        assert s == source
                        assert p.pageNumber < top
                        assert p.pageSize == 1
                        [game]
                }
        ] as CannedGameRepository
        assert game.is(finder.getRandomGame(source))
    }
}
