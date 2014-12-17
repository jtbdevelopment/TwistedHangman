package com.jtbdevelopment.TwistedHangman.security.spring.security

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

import java.security.SecureRandom

/**
 * Date: 12/16/14
 * Time: 12:27 PM
 */
@Component
@CompileStatic
class InjectedPasswordEncoder implements PasswordEncoder {
    private final static Logger logger = LoggerFactory.getLogger(InjectedPasswordEncoder.class)

    final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    InjectedPasswordEncoder() {
        passwordEncoder = new BCryptPasswordEncoder(10, new SecureRandom())
    }

    @Override
    String encode(final CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword)
    }

    @Override
    boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
