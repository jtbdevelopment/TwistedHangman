package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.security.SessionUserInfo
import groovy.transform.CompileStatic
import org.atmosphere.cpr.Action
import org.atmosphere.cpr.AtmosphereConfig
import org.atmosphere.cpr.AtmosphereInterceptor
import org.atmosphere.cpr.AtmosphereResource
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder


/**
 * Date: 12/18/14
 * Time: 6:53 PM
 *
 * Requires
 * <init-param>
 *  <param-name>org.atmosphere.cpr.sessionSupport</param-name>
 *  <param-value>true</param-value>
 * </init-param>
 */
@CompileStatic
class SecuritySessionInterceptor implements AtmosphereInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecuritySessionInterceptor.class)

    @Override
    void configure(final AtmosphereConfig config) {

    }

    @Override
    Action inspect(final AtmosphereResource r) {
        SecurityContextHolder.context = (SecurityContext) r.session().getAttribute("SPRING_SECURITY_CONTEXT")
        ObjectId sessionUserId = ((SessionUserInfo) SecurityContextHolder.context.authentication.principal).effectiveUser.id
        String requestUserId = r.request.pathInfo
        if (requestUserId == "/" + sessionUserId) {
            return Action.CONTINUE
        }
        logger.warn("INVALID REQUEST FOR " + requestUserId + " FROM " + sessionUserId)
        return Action.CANCELLED
    }

    @Override
    void postInspect(final AtmosphereResource r) {

    }
}
