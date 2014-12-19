package com.jtbdevelopment.TwistedHangman.security.spring.social

import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.players.Player
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
 *
 * TODO - lots of cleanup
 * TODO - get images / facebook specific etc etc
 *
 */
@Component
@CompileStatic
class AutoConnectionSignUp implements ConnectionSignUp {
    private final static Logger logger = LoggerFactory.getLogger(AutoConnectionSignUp.class)

    @Autowired
    PlayerRepository playerRepository

    @Override
    String execute(final Connection<?> connection) {
        Player player = playerRepository.findBySourceAndSourceId(connection.key.providerId, connection.key.providerUserId)
        if (player) {
            return player.id
        } else {
            Player p = new Player(
                    disabled: false,
                    displayName: connection.fetchUserProfile().name,
                    source: connection.key.providerId,
                    sourceId: connection.key.providerUserId
            );
            p = playerRepository.save(p);
            return p.id
        }
    }
}
