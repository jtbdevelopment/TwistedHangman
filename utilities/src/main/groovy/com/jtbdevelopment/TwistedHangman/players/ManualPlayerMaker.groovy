package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.TwistedHangman.dao.TwistedHangmanPlayerRepository
import com.jtbdevelopment.gamecore.mongo.players.MongoManualPlayer
import com.jtbdevelopment.gamecore.mongo.players.MongoPlayer
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


        TwistedHangmanPlayerRepository repository = ctx.getBean(TwistedHangmanPlayerRepository.class)
        passwordEncoder = ctx.getBean(PasswordEncoder.class)

        MongoPlayer[] players = [
                makePlayer("Manual Player1", 'M1@MANUAL.COM', "M1"),
                makePlayer("Manual Player2", 'M2@MANUAL.COM', "M2"),
                makePlayer("Manual Player3", 'M3@MANUAL.COM', "M3"),
                makePlayer("Manual Player4", 'M4@MANUAL.COM', "M4"),
        ]

        players.each {
            MongoPlayer it ->
                MongoPlayer loaded = repository.findBySourceAndSourceId(it.source, it.sourceId);
                if (!loaded) {
                    println "Creating player " + it
                    repository.save(it)
                } else {
                    println "Player already created " + it
                }
        }

        println "Complete"
    }

    static MongoManualPlayer makePlayer(final String displayName, final String sourceId, final String password) {
        return new MongoManualPlayer(
                disabled: false,
                adminUser: true,
                verified: true,
                displayName: displayName,
                sourceId: sourceId,
                password: passwordEncoder.encode(password)
        );
    }
}
