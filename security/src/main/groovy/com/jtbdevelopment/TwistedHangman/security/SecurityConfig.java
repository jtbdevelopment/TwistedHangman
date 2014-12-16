package com.jtbdevelopment.TwistedHangman.security;

import com.jtbdevelopment.TwistedHangman.security.spring.security.InjectedPasswordEncoder;
import com.jtbdevelopment.TwistedHangman.security.spring.security.PlayerUserDetailsService;
import com.jtbdevelopment.TwistedHangman.security.spring.social.PlayerSocialUserDetailsService;
import groovy.transform.CompileStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.security.SpringSocialConfigurer;
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(injectedPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(playerUserDetailsService);
        auth.authenticationProvider(daoAuthenticationProvider);
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
