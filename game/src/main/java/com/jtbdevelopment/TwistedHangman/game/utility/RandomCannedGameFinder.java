package com.jtbdevelopment.TwistedHangman.game.utility;

import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository;
import com.jtbdevelopment.TwistedHangman.exceptions.system.PreMadePuzzleFinderException;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Date: 11/2/14 Time: 8:21 AM
 */
@Component
public class RandomCannedGameFinder {

  private static final Logger logger = LoggerFactory.getLogger(RandomCannedGameFinder.class);
  private final Random random = new Random();
  private final PreMadePuzzleRepository repository;

  public RandomCannedGameFinder(
      final PreMadePuzzleRepository repository) {
    this.repository = repository;
  }

  public PreMadePuzzle getRandomGame(final String source) {
    if (StringUtils.isEmpty(source)) {
      long count = repository.count();
      int index = getRandomIndex(count);

      Page<PreMadePuzzle> all = repository.findAll(PageRequest.of(index, 1));

      List<PreMadePuzzle> content = all.getContent();
      return extractGame(content);
    } else {
      long max = repository.countBySource(source);
      int index = getRandomIndex(max);
      return extractGame(repository.findBySource(source, PageRequest.of(index, 1)));
    }

  }

  public PreMadePuzzle getRandomGame() {
    return getRandomGame("");
  }

  private int getRandomIndex(long count) {
    if (count < 1) {
      logger.warn("No random games! (" + count + ")");
      throw new PreMadePuzzleFinderException(PreMadePuzzleFinderException.NO_GAMES_FOUND);
    }

    if (count <= Integer.MAX_VALUE) {
      return random.nextInt(((int) count));
    } else {
      return random.nextInt(Integer.MAX_VALUE);
    }

  }

  private PreMadePuzzle extractGame(final List<PreMadePuzzle> games) {
    if (games.size() == 1) {
      return games.get(0);
    } else {
      logger.warn("Failed to load random games! (" + games + ")");
      throw new PreMadePuzzleFinderException(PreMadePuzzleFinderException.GAME_NOT_LOADED);
    }

  }
}
