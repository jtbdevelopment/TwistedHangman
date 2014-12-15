package com.jtbdevelopment.TwistedHangman.security;

import groovy.transform.CompileStatic;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.*;

/**
 * Date: 12/14/14
 * Time: 7:07 PM
 * <p>
 * TODO - lots of cleanup
 * TODO - may not be necessary since addin is not working
 */
@CompileStatic
public class CustomSpringSocialConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private UserIdSource userIdSource;
    private String postLoginUrl;
    private String postFailureUrl;
    private boolean alwaysUsePostLoginUrl = false;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        UsersConnectionRepository usersConnectionRepository = getDependency(applicationContext, UsersConnectionRepository.class);
        SocialAuthenticationServiceLocator authServiceLocator = getDependency(applicationContext, SocialAuthenticationServiceLocator.class);
        SocialUserDetailsService socialUsersDetailsService = getDependency(applicationContext, SocialUserDetailsService.class);

        SocialAuthenticationFilter filter = new SocialAuthenticationFilter(http.getSharedObject(AuthenticationManager.class), userIdSource != null ? userIdSource : new AuthenticationNameUserIdSource(), usersConnectionRepository, authServiceLocator);

        RememberMeServices rememberMe = http.getSharedObject(RememberMeServices.class);
        if (rememberMe != null) {
            filter.setRememberMeServices(rememberMe);
        }


        if (postLoginUrl != null) {
            filter.setPostLoginUrl(postLoginUrl);
            filter.setAlwaysUsePostLoginUrl(alwaysUsePostLoginUrl);
        }


        if (postFailureUrl != null) {
            filter.setPostFailureUrl(postFailureUrl);
        }


        filter.setAuthenticationSuccessHandler(new AddInfoAuthenticationSuccessHandler());

        http.authenticationProvider(new SocialAuthenticationProvider(usersConnectionRepository, socialUsersDetailsService)).addFilterBefore(postProcess(filter), AbstractPreAuthenticatedProcessingFilter.class);
    }

    private <T> T getDependency(ApplicationContext applicationContext, Class<T> dependencyType) {
        try {
            T dependency = applicationContext.getBean(dependencyType);
            return dependency;
        } catch (NoSuchBeanDefinitionException e) {
            throw new IllegalStateException("CustomSpringSocialConfigurer depends on " + dependencyType.getName() + ". No single bean of that type found in application context.", e);
        }

    }

    /**
     * Sets the {@link UserIdSource} to use for authentication. Defaults to {@link org.springframework.social.security.AuthenticationNameUserIdSource}.
     */
    public CustomSpringSocialConfigurer userIdSource(UserIdSource userIdSource) {
        this.userIdSource = userIdSource;
        return this;
    }

    /**
     * Sets the URL to land on after a successful login.
     */
    public CustomSpringSocialConfigurer postLoginUrl(String postLoginUrl) {
        this.postLoginUrl = postLoginUrl;
        return this;
    }

    /**
     * If true, always redirect to postLoginUrl, even if a pre-signin target is in the request cache.
     */
    public CustomSpringSocialConfigurer alwaysUsePostLoginUrl(boolean alwaysUsePostLoginUrl) {
        this.alwaysUsePostLoginUrl = alwaysUsePostLoginUrl;
        return this;
    }

    /**
     * Sets the URL to land on after a failed login.
     */
    public CustomSpringSocialConfigurer postFailureUrl(String postFailureUrl) {
        this.postFailureUrl = postFailureUrl;
        return this;
    }
}
