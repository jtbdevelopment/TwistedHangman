package com.jtbdevelopment.TwistedHangman.security.spring

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder
import org.springframework.stereotype.Component

/**
 * Date: 12/16/14
 * Time: 12:27 PM
 */
@Component
@CompileStatic
class InjectedPasswordEncoder implements PasswordEncoder {
    private final static Logger logger = LoggerFactory.getLogger(InjectedPasswordEncoder.class)

    final StandardPasswordEncoder standardPasswordEncoder;

    @Autowired
    InjectedPasswordEncoder(@Value('${password.salt:SALTED}') final String salt) {
        if (salt == 'SALTED') {
            logger.warn('Using default SALT!')
        }
        standardPasswordEncoder = new StandardPasswordEncoder(salt)
    }

    @Override
    String encode(final CharSequence rawPassword) {
        return standardPasswordEncoder.encode(rawPassword)
    }

    @Override
    boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return standardPasswordEncoder.matches(rawPassword, encodedPassword)
    }
}
