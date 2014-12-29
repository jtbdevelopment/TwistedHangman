package com.jtbdevelopment.TwistedHangman.feed.websocket

import com.jtbdevelopment.TwistedHangman.players.Player
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import org.atmosphere.cpr.Action
import org.atmosphere.cpr.AtmosphereRequest
import org.atmosphere.cpr.AtmosphereResource
import org.bson.types.ObjectId
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext

import javax.servlet.http.HttpSession

/**
 * Date: 12/22/14
 * Time: 6:33 PM
 */
class SpringSecuritySessionInterceptorTest extends GroovyTestCase {
    private SpringSecuritySessionInterceptor interceptor = new SpringSecuritySessionInterceptor()

    void testInspectWithCorrectDetails() {
        ObjectId id = new ObjectId()
        AtmosphereRequest request = new AtmosphereRequest.Builder().pathInfo("/" + id.toHexString()).build()
        SecurityContext sc = [
                getAuthentication: {
                    return [
                            getPrincipal: {
                                return {
                                    getEffectiveUser:
                                    {
                                        return {
                                            getId:
                                            {
                                                return id;
                                            }
                                        } as Player
                                    }
                                } as SessionUserInfo
                            }
                    ] as Authentication
                }
        ] as SecurityContext
        HttpSession s = [
                getAttribute: {
                    String name ->
                        assert name == SpringSecuritySessionInterceptor.SPRING__SECURITY__CONTEXT
                        return sc;
                }
        ] as HttpSession
        AtmosphereResource r = [
                session   : {
                    return s
                },
                getRequest: {
                    return request
                }
        ] as AtmosphereResource
        assert Action.CONTINUE == interceptor.inspect(r)
    }

    void testInspectWithIncorrectDetails() {
        ObjectId id = new ObjectId()
        AtmosphereRequest request = new AtmosphereRequest.Builder().pathInfo("/X" + id.toHexString()).build()
        SecurityContext sc = [
                getAuthentication: {
                    return [
                            getPrincipal: {
                                return {
                                    getEffectiveUser:
                                    {
                                        return {
                                            getId:
                                            {
                                                return id;
                                            }
                                        } as Player
                                    }
                                } as SessionUserInfo
                            }
                    ] as Authentication
                }
        ] as SecurityContext
        HttpSession s = [
                getAttribute: {
                    String name ->
                        assert name == SpringSecuritySessionInterceptor.SPRING__SECURITY__CONTEXT
                        return sc;
                }
        ] as HttpSession
        AtmosphereResource r = [
                session   : {
                    return s
                },
                getRequest: {
                    return request
                }
        ] as AtmosphereResource
        assert Action.CANCELLED == interceptor.inspect(r)
    }

    void testInspectWithNullContext() {
        ObjectId id = new ObjectId()
        AtmosphereRequest request = new AtmosphereRequest.Builder().pathInfo("/" + id.toHexString()).build()
        HttpSession s = [
                getAttribute: {
                    String name ->
                        assert name == SpringSecuritySessionInterceptor.SPRING__SECURITY__CONTEXT
                        return null;
                }
        ] as HttpSession
        AtmosphereResource r = [
                session   : {
                    return s
                },
                getRequest: {
                    return request
                }
        ] as AtmosphereResource
        assert Action.CANCELLED == interceptor.inspect(r)
    }

    void testInspectWithNullSession() {
        ObjectId id = new ObjectId()
        AtmosphereRequest request = new AtmosphereRequest.Builder().pathInfo("/" + id.toHexString()).build()
        SecurityContext sc = [
                getAuthentication: {
                    return null
                }
        ] as SecurityContext
        HttpSession s = [
                getAttribute: {
                    String name ->
                        assert name == SpringSecuritySessionInterceptor.SPRING__SECURITY__CONTEXT
                        return sc;
                }
        ] as HttpSession
        AtmosphereResource r = [
                session   : {
                    return s
                },
                getRequest: {
                    return request
                }
        ] as AtmosphereResource
        assert Action.CANCELLED == interceptor.inspect(r)
    }

}
