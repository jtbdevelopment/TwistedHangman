package com.jtbdevelopment.TwistedHangman.utilities.playerCreator

import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoManualPlayer
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.mongo.players.MongoPlayerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Date: 11/22/2014
 * Time: 1:48 PM
 */
class ManualPlayerMaker {
    static PasswordEncoder passwordEncoder;

    static MongoPlayerFactory playerFactory

    public static void main(final String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-game.xml");
        ctx.refresh();


        AbstractPlayerRepository repository = ctx.getBean(AbstractPlayerRepository.class)
        playerFactory = ctx.getBean(MongoPlayerFactory.class)
        passwordEncoder = ctx.getBean(PasswordEncoder.class)

        MongoPlayer[] players = [
                makePlayer("Manual Player1", 'M1@MANUAL.COM', "M1"),
                makePlayer("Manual Player2", 'M2@MANUAL.COM', "M2"),
                makePlayer("Manual Player3", 'M3@MANUAL.COM', "M3"),
                makePlayer("Manual Player4", 'M4@MANUAL.COM', "M4"),
        ]

        players.each {
            MongoPlayer it ->
                MongoPlayer loaded = (MongoPlayer) repository.findBySourceAndSourceId(it.source, it.sourceId);
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
        MongoManualPlayer manualPlayer = (MongoManualPlayer) playerFactory.newManualPlayer()
        manualPlayer.disabled = false
        manualPlayer.adminUser = true
        manualPlayer.verified = true
        manualPlayer.displayName = displayName
        manualPlayer.sourceId = sourceId
        manualPlayer.password = passwordEncoder.encode(password)
        return manualPlayer
    }
}
