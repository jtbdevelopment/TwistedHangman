package com.jtbdevelopment.TwistedHangman.security.spring.social

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
import org.springframework.social.security.SocialAuthenticationServiceRegistry
import org.springframework.social.security.provider.SocialAuthenticationService

/**
 * Date: 12/16/14
 * Time: 12:56 PM
 */
@Configuration
@CompileStatic
class SpringSocialConfig {
    @Bean
    @Autowired
    SocialAuthenticationServiceRegistry connectionFactoryLocator(final List<SocialAuthenticationService> services) {
        SocialAuthenticationServiceRegistry socialAuthenticationServiceRegistry = new SocialAuthenticationServiceRegistry();
        for (final SocialAuthenticationService service : services) {
            socialAuthenticationServiceRegistry.addAuthenticationService(service);
        }
        return socialAuthenticationServiceRegistry;
    }

    @Bean
    @Autowired
    UsersConnectionRepository usersConnectionRepository(
            final ConnectionFactoryLocator connectionFactoryLocator,
            final AutoConnectionSignUp autoConnectionSignUp) {
        InMemoryUsersConnectionRepository inMemoryUsersConnectionRepository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
        inMemoryUsersConnectionRepository.setConnectionSignUp(autoConnectionSignUp);
        return inMemoryUsersConnectionRepository;
    }
}
