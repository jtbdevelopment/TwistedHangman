package com.jtbdevelopment.TwistedHangman.security;

import com.jtbdevelopment.TwistedHangman.security.facebook.FacebookProperties;
import groovy.transform.CompileStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.social.security.provider.SocialAuthenticationService;

import java.util.List;
// TODO - lots of cleanup

@Configuration
@EnableWebSecurity
@CompileStatic
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
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
                .formLogin().loginPage("/signin/signin.html").failureUrl("/signin/signin.html").successHandler(new AddInfoAuthenticationSuccessHandler())
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key(tokenKey)
                .and()
                .apply(new CustomSpringSocialConfigurer().postFailureUrl("/signin/signin.html"))
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
