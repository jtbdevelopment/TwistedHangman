package com.jtbdevelopment.gamecore.security.spring.social.connect

import com.jtbdevelopment.gamecore.dao.AbstractPlayerRepository
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerInt
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionSignUp
import org.springframework.stereotype.Component

/**
 * Date: 12/14/14
 * Time: 5:11 PM
 */
@Component
@CompileStatic
class AutoConnectionSignUp implements ConnectionSignUp {
    private final static Logger logger = LoggerFactory.getLogger(AutoConnectionSignUp.class)

    @Autowired
    AbstractPlayerRepository playerRepository

    @Override
    String execute(final Connection<?> connection) {
        PlayerInt player = playerRepository.findBySourceAndSourceId(connection.key.providerId, connection.key.providerUserId)
        if (player) {
            return player.id
        } else {
            Player p = new Player(
                    disabled: false,
                    displayName: connection.displayName,
                    source: connection.key.providerId,
                    sourceId: connection.key.providerUserId,
                    profileUrl: connection.profileUrl,
                    imageUrl: connection.imageUrl
            );
            p = playerRepository.save(p);
            return p.idAsString
        }
    }
}