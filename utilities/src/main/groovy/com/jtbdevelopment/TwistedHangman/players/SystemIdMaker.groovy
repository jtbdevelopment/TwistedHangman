package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Date: 11/3/14
 * Time: 7:02 AM
 */
class SystemIdMaker {

    public static void main(final String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-game.xml");
        ctx.refresh();

        PlayerRepository repository = ctx.getBean(PlayerRepository.class)

        Player player = repository.findOne(Player.SYSTEM_ID_ID)
        if (player == null) {
            println "Making system id"
            repository.save(Player.SYSTEM_PLAYER)
        } else {
            println "Verifying system id"
            assert player.source == Player.SYSTEM_ID_SOURCE
            assert player.displayName == Player.SYSTEM_ID_DISPLAY_NAME
        }

        println "Complete"
    }
}
