package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Date: 11/22/2014
 * Time: 1:48 PM
 */
class ManualPlayerMaker {
    public static void main(final String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-game.xml");
        ctx.refresh();


        PlayerRepository repository = ctx.getBean(PlayerRepository.class)

        Player[] players = [new Player(id: "MANUAL1", disabled: false, displayName: "Manual Player1", source: "MANUAL"),
                            new Player(id: "MANUAL2", disabled: false, displayName: "Manual Player2", source: "MANUAL"),
                            new Player(id: "MANUAL3", disabled: false, displayName: "Manual Player3", source: "MANUAL"),
                            new Player(id: "MANUAL4", disabled: false, displayName: "Manual Player4", source: "MANUAL")]

        players.each {
            Player it ->
                Player loaded = repository.findOne(it.id);
                if (loaded == null) {
                    println "Creating player " + it
                    repository.save(it)
                } else {
                    println "Player already created " + it
                }
        }

        println "Complete"
    }
}
