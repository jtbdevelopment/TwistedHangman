package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository
import com.jtbdevelopment.TwistedHangman.exceptions.system.RandomCannedGameFinderException
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Date: 11/2/14
 * Time: 8:21 AM
 */
@Component
@CompileStatic
class RandomCannedGameFinder {
    private static final Logger logger = LoggerFactory.getLogger(RandomCannedGameFinder.class)

    @Autowired
    PreMadePuzzleRepository repository

    private final Random random = new Random()

    public PreMadePuzzle getRandomGame(final String source = "") {
        if (StringUtils.isEmpty(source)) {
            long count = repository.count()
            int index = getRandomIndex(count)

            Page<PreMadePuzzle> all = repository.findAll(new PageRequest(index, 1))

            List<PreMadePuzzle> content = all.content
            return extractGame(content)
        } else {
            long max = repository.countBySource(source)
            int index = getRandomIndex(max)
            return extractGame(repository.findBySource(source, new PageRequest(index, 1)))
        }
    }

    protected int getRandomIndex(long count) {
        if (count < 1) {
            logger.warn("No random games! (" + count + ")")
            throw new RandomCannedGameFinderException(RandomCannedGameFinderException.NO_GAMES_FOUND)
        }
        if (count <= Integer.MAX_VALUE) {
            return random.nextInt(((int) count))
        } else {
            return random.nextInt(Integer.MAX_VALUE)
        }
    }

    static protected PreMadePuzzle extractGame(final List<PreMadePuzzle> games) {
        if (games.size() == 1) {
            return games[0]
        } else {
            logger.warn("Failed to load random games! (" + games + ")")
            throw new RandomCannedGameFinderException(RandomCannedGameFinderException.GAME_NOT_LOADED)
        }
    }
}
