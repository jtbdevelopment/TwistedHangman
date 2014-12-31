package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository
import com.jtbdevelopment.TwistedHangman.exceptions.system.RandomCannedGameFinderException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

/**
 * Date: 11/2/14
 * Time: 4:01 PM
 */
class RandomPreMadePuzzleFinderTest extends GroovyTestCase {
    RandomCannedGameFinder finder = new RandomCannedGameFinder()


    public void testReturnsARandomGameFromSingleItemUniverse() {
        PreMadePuzzle game = [] as PreMadePuzzle
        Page<PreMadePuzzle> page = [getContent: { [game] }] as Page<PreMadePuzzle>
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
        ] as PreMadePuzzleRepository
        assert game.is(finder.getRandomGame())
    }


    public void testExceptionIfNoGames() {
        finder.repository = [
                count: {
                    0L
                }
        ] as PreMadePuzzleRepository
        try {
            finder.getRandomGame()
        } catch (RandomCannedGameFinderException e) {
            assert e.message == "No pre-made games were found."
        }
    }


    public void testExceptionIfNoContent() {
        Page<PreMadePuzzle> page = [getContent: { [] }] as Page<PreMadePuzzle>
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
        ] as PreMadePuzzleRepository
        try {
            finder.getRandomGame()
        } catch (RandomCannedGameFinderException e) {
            assert e.message == "Pre-made game failed to load."
        }
    }


    public void testExceptionIfTooMuchContent() {
        PreMadePuzzle game = [] as PreMadePuzzle
        Page<PreMadePuzzle> page = [getContent: { [game, game] }] as Page<PreMadePuzzle>
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
        ] as PreMadePuzzleRepository
        try {
            finder.getRandomGame()
        } catch (RandomCannedGameFinderException e) {
            assert e.message == "Pre-made game failed to load."
        }
    }


    public void testReturnsARandomGameFromLargeUniverse() {
        PreMadePuzzle game = [] as PreMadePuzzle
        long top = Long.MAX_VALUE / 2
        Page<PreMadePuzzle> page = [getContent: { [game] }] as Page<PreMadePuzzle>
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
        ] as PreMadePuzzleRepository
        assert game.is(finder.getRandomGame())
    }


    public void testReturnsARandomGameFromWithSource() {
        String source = "Interwebs"
        PreMadePuzzle game = [] as PreMadePuzzle
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
        ] as PreMadePuzzleRepository
        assert game.is(finder.getRandomGame(source))
    }
}
