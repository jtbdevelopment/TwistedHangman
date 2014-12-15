package com.jtbdevelopment.TwistedHangman.security

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Date: 12/14/14
 * Time: 6:04 PM
 *
 * TODO - not working - new idea
 */
@CompileStatic
class AddInfoAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) throws ServletException, IOException {
        Object principal = SecurityContextHolder.context.authentication.principal
        if (principal in PlayerUserDetails) {
            Cookie cookie = new Cookie("playerInfo", objectMapper.writeValueAsString(((PlayerUserDetails) principal).player));
            response.addCookie(cookie)
        }
        super.onAuthenticationSuccess(request, response, authentication)
        if (principal in PlayerUserDetails) {
            Cookie cookie = new Cookie("playerInfo", objectMapper.writeValueAsString(((PlayerUserDetails) principal).player));
            response.addCookie(cookie)
        }
    }
}
