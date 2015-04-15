package com.jtbdevelopment.TwistedHangman.utilities.phrasegatherer.wofs

import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository
import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle
import com.jtbdevelopment.games.dictionary.Validator
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Date: 11/1/14
 * Time: 6:43 PM
 */
class WheelOfFortuneSolutionsLoader {
    public static void main(final String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.jtbdevelopment");

        PreMadePuzzleRepository repository = ctx.getBean(PreMadePuzzleRepository.class)
        Validator validator = ctx.getBean(Validator.class)

        String WOFS = "WOFS"
        repository.removeBySource(WOFS)
        WheelOfFortuneSolutionsParser.parseTimeframe(10, 2009, 12, 2014, {
            String category, String item ->
                if (category && item) {
                    def c = category.toUpperCase()
                    def i = item.toUpperCase()
                    def vc = validator.validateWordPhrase(c)
                    def vi = validator.validateWordPhrase(i)
                    if (vc.isEmpty() && vi.isEmpty()) {
                        repository.save(new PreMadePuzzle(source: WOFS, category: c, wordPhrase: i))
                    } else {
                        println "category validator " + vc + " / phrase validaotr " + vi
                    }
                }
        })
        ctx.stop()
    }
}
