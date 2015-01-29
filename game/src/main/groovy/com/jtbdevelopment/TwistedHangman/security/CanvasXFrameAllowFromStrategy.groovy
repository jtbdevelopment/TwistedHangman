package com.jtbdevelopment.TwistedHangman.security

import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy

import javax.servlet.http.HttpServletRequest

/**
 * Date: 1/27/2015
 * Time: 2:24 PM
 */
class CanvasXFrameAllowFromStrategy implements AllowFromStrategy {
    @Override
    String getAllowFromValue(final HttpServletRequest request) {
        return "https://apps.facebook.com"
    }
}