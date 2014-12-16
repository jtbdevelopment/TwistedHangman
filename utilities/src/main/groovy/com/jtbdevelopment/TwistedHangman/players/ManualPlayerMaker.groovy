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

        Player[] players = [new ManualPlayer(disabled: false, displayName: "Manual Player1", sourceId: 'MANUAL1@MANUAL.COM', verified: true),
                            new ManualPlayer(disabled: false, displayName: "Manual Player2", sourceId: 'MANUAL2@MANUAL.COM', verified: true),
                            new ManualPlayer(disabled: false, displayName: "Manual Player3", sourceId: 'MANUAL3@MANUAL.COM', verified: true),
                            new ManualPlayer(disabled: false, displayName: "Manual Player4", sourceId: 'MANUAL4@MANUAL.COM', verified: true)]

        players.each {
            Player it ->
                List<Player> loaded = repository.findBySourceAndSourceId(it.source, it.sourceId);
                if (loaded.size() == 0) {
                    println "Creating player " + it
                    repository.save(it)
                } else {
                    println "Player already created " + it
                }
        }

        println "Complete"
    }
}
