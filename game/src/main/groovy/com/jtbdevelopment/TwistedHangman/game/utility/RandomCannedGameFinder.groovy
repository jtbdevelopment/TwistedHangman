package com.jtbdevelopment.TwistedHangman.game.utility

import com.jtbdevelopment.TwistedHangman.dao.CannedGameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Date: 11/2/14
 * Time: 8:21 AM
 */
@Component
class RandomCannedGameFinder {
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
            throw new IllegalStateException("Too many choices!")
        }
        if (max < 1) {
            throw new IllegalStateException("Not enough choices!")
        }
        return random.nextInt((int) max)
    }

    static protected CannedGame extractGame(final List<CannedGame> games) {
        if (games.size() == 1) {
            return games[0]
        } else {
            throw new IllegalStateException("Expected a single game! Not " + games.size())
        }
    }
}
