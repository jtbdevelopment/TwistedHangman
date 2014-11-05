package com.jtbdevelopment.TwistedHangman.phrasegatherer.wos

import com.jtbdevelopment.TwistedHangman.dao.CannedGameRepository
import com.jtbdevelopment.TwistedHangman.dictionary.Validator
import com.jtbdevelopment.TwistedHangman.game.utility.CannedGame
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Date: 11/1/14
 * Time: 6:43 PM
 */
class WheelOfFortuneSolutionsLoader {
    public static void main(final String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-game.xml");
        ctx.refresh();

        CannedGameRepository repository = ctx.getBean(CannedGameRepository.class)
        Validator validator = ctx.getBean(Validator.class)

        String WOFS = "WOFS"
        repository.removeBySource(WOFS)
        WheelOfFortuneSolutionsParser.parseTimeframe(10, 2009, 12, 2013, {
            String category, String item ->
                if (category && item) {
                    def c = category.toUpperCase()
                    def i = item.toUpperCase()
                    def vc = validator.validateWordPhrase(c)
                    def vi = validator.validateWordPhrase(i)
                    if (vc == null && vi == null) {
                        repository.save(new CannedGame(source: WOFS, category: c, wordPhrase: i))
                    } else {
                        println "category validator " + vc + " / phrase validaotr " + vi
                    }
                }
        })
    }
}
