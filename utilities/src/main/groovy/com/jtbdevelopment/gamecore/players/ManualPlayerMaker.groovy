package com.jtbdevelopment.gamecore.players

import com.jtbdevelopment.gamecore.dao.PlayerRepository
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Date: 11/22/2014
 * Time: 1:48 PM
 */
class ManualPlayerMaker {
    static PasswordEncoder passwordEncoder;

    public static void main(final String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-game.xml");
        ctx.refresh();


        PlayerRepository repository = ctx.getBean(PlayerRepository.class)
        passwordEncoder = ctx.getBean(PasswordEncoder.class)

        Player[] players = [
                makePlayer("Manual Player1", 'M1@MANUAL.COM', "M1"),
                makePlayer("Manual Player2", 'M2@MANUAL.COM', "M2"),
                makePlayer("Manual Player3", 'M3@MANUAL.COM', "M3"),
                makePlayer("Manual Player4", 'M4@MANUAL.COM', "M4"),
        ]

        players.each {
            Player it ->
                Player loaded = repository.findBySourceAndSourceId(it.source, it.sourceId);
                if (!loaded) {
                    println "Creating player " + it
                    repository.save(it)
                } else {
                    println "Player already created " + it
                }
        }

        println "Complete"
    }

    static ManualPlayer makePlayer(final String displayName, final String sourceId, final String password) {
        return new ManualPlayer(
                disabled: false,
                adminUser: true,
                verified: true,
                displayName: displayName,
                sourceId: sourceId,
                password: passwordEncoder.encode(password)
        );
    }
}
