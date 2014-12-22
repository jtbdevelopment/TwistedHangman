package com.jtbdevelopment.TwistedHangman.security;

import com.jtbdevelopment.TwistedHangman.security.spring.security.InjectedPasswordEncoder;
import com.jtbdevelopment.TwistedHangman.security.spring.security.PlayerUserDetailsService;
import com.jtbdevelopment.TwistedHangman.security.spring.social.PlayerSocialUserDetailsService;
import groovy.transform.CompileStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.security.SpringSocialConfigurer;
// TODO - lots of cleanup

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@CompileStatic
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    PlayerUserDetailsService playerUserDetailsService;

    @Autowired
    PlayerSocialUserDetailsService playerSocialUserDetailsService;

    @Autowired
    InjectedPasswordEncoder injectedPasswordEncoder;

    @Autowired
    SecurityProperties securityProperties;

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
        PortMapperImpl portMapper = new PortMapperImpl();
        portMapper.getTranslatedPortMappings().put(8998, 8999);
        portMapper.getTranslatedPortMappings().put(9998, 9999);
        http
                .authorizeRequests()
                        //  TODO - review signup/disconnect
                .antMatchers("/favicon.ico", "/images/**", "/facebook/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin().loginPage("/signin/signin.html").loginProcessingUrl("/signin/authenticate").failureUrl("/signin/signin.html?error=BadCredentials")
                .and()
                .logout().logoutUrl("/signout").deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key(tokenKey)
                .and()
                .portMapper().portMapper(portMapper)
                .and()
                        //  TODO - what?
                .csrf().disable()
                //  TODO - better answer for this for canvas
                .headers().frameOptions().disable()
                .apply(new SpringSocialConfigurer());

        if (Boolean.parseBoolean(securityProperties.getAllowBasicAuth())) {
            logger.warn("-----------------------------------------------------");
            logger.warn("-----------------------------------------------------");
            logger.warn("-----------------------------------------------------");
            logger.warn("Allowing Basic Auth!  Should only be in test systems!");
            logger.warn("Disabling https requirements as well!                ");
            logger.warn("-----------------------------------------------------");
            logger.warn("-----------------------------------------------------");
            logger.warn("-----------------------------------------------------");
            http.httpBasic();
        } else {
            http.requiresChannel().antMatchers("/**").requiresSecure();

        }
        TokenBasedRememberMeServices twistedHangman = new TokenBasedRememberMeServices(tokenKey, http.getSharedObject(UserDetailsService.class));
//        twistedHangman.setAlwaysRemember(true);
        http.rememberMe().rememberMeServices(twistedHangman);
    }
}
