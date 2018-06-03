package com.jtbdevelopment.TwistedHangman.utilities.phrasegatherer.wofs;

import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository;
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import com.jtbdevelopment.games.dictionary.Validator;
import com.mchange.v2.lang.StringUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Date: 11/1/14 Time: 6:43 PM
 */
public class WheelOfFortuneSolutionsLoader {

  private static final Logger logger = LoggerFactory.getLogger(WheelOfFortuneSolutionsLoader.class);

  public static void main(final String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext("com.jtbdevelopment");

    final PreMadePuzzleRepository repository = ctx.getBean(PreMadePuzzleRepository.class);
    final Validator validator = ctx.getBean(Validator.class);

    final String WOFS = "WOFS";
    repository.removeBySource(WOFS);
    AtomicInteger counter = new AtomicInteger();
    WheelOfFortuneSolutionsParser
        .parseTimeframe(10, 2009, 12, 2017, (category, item) -> {
          if (StringUtils.nonEmptyString(category) && StringUtils.nonEmptyString(item)) {
            String c = category.toUpperCase();
            String i = item.toUpperCase();
            List<String> vc = validator.validateWordPhrase(c, DictionaryType.USEnglishMaximum);
            List<String> vi = validator.validateWordPhrase(i, DictionaryType.USEnglishMaximum);
            if (vc.isEmpty() && vi.isEmpty()) {

              PreMadePuzzle puzzle = new PreMadePuzzle();
              puzzle.setSource(WOFS);
              puzzle.setCategory(c);
              puzzle.setWordPhrase(i);
              logger.info("Accepting {}/{}", c, i);
              counter.incrementAndGet();
//              repository.save(puzzle);
            }

          }
        });
    ((AnnotationConfigApplicationContext) ctx).stop();
    logger.info("Accepted {} puzzles", counter.get());
  }

}
