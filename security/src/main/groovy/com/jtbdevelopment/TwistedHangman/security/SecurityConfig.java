package com.jtbdevelopment.TwistedHangman.security;

import com.jtbdevelopment.TwistedHangman.security.facebook.AutoConnectionSignUp;
import com.jtbdevelopment.TwistedHangman.security.facebook.FacebookProperties;
import com.jtbdevelopment.TwistedHangman.security.spring.InjectedPasswordEncoder;
import com.jtbdevelopment.TwistedHangman.security.spring.PlayerSocialUserDetailsService;
import com.jtbdevelopment.TwistedHangman.security.spring.PlayerUserDetailsService;
import groovy.transform.CompileStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.facebook.security.FacebookAuthenticationService;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.social.security.provider.SocialAuthenticationService;

import java.util.List;
// TODO - lots of cleanup

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@CompileStatic
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    PlayerUserDetailsService playerUserDetailsService;

    @Autowired
    PlayerSocialUserDetailsService playerSocialUserDetailsService;

    @Autowired
    InjectedPasswordEncoder injectedPasswordEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(injectedPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(playerUserDetailsService);
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Bean
    @Autowired
    FacebookAuthenticationService facebookAuthenticationService(final FacebookProperties facebookProperties) {
        return new FacebookAuthenticationService(facebookProperties.getClientID(), facebookProperties.getClientSecret());
    }

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

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        String tokenKey = "TwistedHangman";
        http
                .authorizeRequests()
                        //  TODO - review signup/disconnect
                .antMatchers("/favicon.ico", "/images/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin().loginPage("/signin/signin.html").loginProcessingUrl("/signin/authenticate").failureUrl("/signin/signin.html")
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key(tokenKey)
                .and()
                        //  TODO - what?
                .csrf().disable()
//                .apply(new CustomSpringSocialConfigurer().postFailureUrl("/signin/signin.html"))
                .apply(new SpringSocialConfigurer())
//                .and()
//                .httpBasic()
        ;
        TokenBasedRememberMeServices twistedHangman = new TokenBasedRememberMeServices(tokenKey, http.getSharedObject(UserDetailsService.class));
//        twistedHangman.setAlwaysRemember(true);
        http.rememberMe().rememberMeServices(twistedHangman);

        /*
        http
                .formLogin().loginPage("/signin").loginProcessingUrl("/signin/authenticate").failureUrl("/signin?param.error=bad_credentials")
                .and()
                .logout().logoutUrl("/signout").deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .rememberMe()
                .and()
                .apply(new SpringSocialConfigurer())
        ;
        */
    }
}
