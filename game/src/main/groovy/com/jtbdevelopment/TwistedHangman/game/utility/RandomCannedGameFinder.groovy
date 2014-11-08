package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.dao.CannedGameRepository
import com.jtbdevelopment.TwistedHangman.exceptions.RandomCannedGameFinderException
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
    private CannedGameRepository repository

    private final Random random = new Random()

    public CannedGame getRandomGame(final String source = "") {
        new Random().ints()
        if (StringUtils.isEmpty(source)) {
            long max = repository.count()
            int index = getRandomIndex(max)
            return extractGame(repository.findAll(new PageRequest(index, 1)).content)
        } else {
            long max = repository.countBySource(source)
            int index = getRandomIndex(max)
            return extractGame(repository.findBySource(source, new PageRequest(index, 1)))
        }
    }

    protected int getRandomIndex(long max) {
        if (max > Integer.MAX_VALUE) {
            logger.warn("More than max integer random games! (" + max + ")")
            max = Integer.MAX_VALUE
        }
        if (max < 1) {
            logger.warn("No random games! (" + max + ")")
            throw new RandomCannedGameFinderException(RandomCannedGameFinderException.NO_GAMES_FOUND)
        }
        return random.nextInt((int) max)
    }

    static protected CannedGame extractGame(final List<CannedGame> games) {
        if (games.size() == 1) {
            return games[0]
        } else {
            logger.warn("Failed to load random games! (" + games + ")")
            throw new RandomCannedGameFinderException(RandomCannedGameFinderException.GAME_NOT_LOADED)
        }
    }
}
