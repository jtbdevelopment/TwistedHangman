package com.jtbdevelopment.gamecore.security.rest

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.gamecore.players.Player
import com.jtbdevelopment.gamecore.players.PlayerRoles
import com.jtbdevelopment.gamecore.security.SessionUserInfo
import groovy.transform.TypeChecked
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.lang.reflect.Method

/**
 * Date: 12/24/14
 * Time: 2:57 PM
 */
class SecurityServiceTest extends TwistedHangmanTestCase {
    SecurityService service = new SecurityService()

    void testClassAnnotations() {
        assert SecurityService.class.isAnnotationPresent(RolesAllowed.class)
        assert SecurityService.class.getAnnotation(RolesAllowed.class).value() == [PlayerRoles.PLAYER]
        assert SecurityService.class.isAnnotationPresent(Path.class)
        assert SecurityService.class.getAnnotation(Path.class).value() == "security"
    }

    void testGetSessionPlayer() {
        SecurityContextHolder.context = new SecurityContextImpl()
        SecurityContextHolder.context.authentication = new TestingAuthenticationToken(
                new SessionUserInfo() {
                    @Override
                    Player getSessionUser() {
                        return PONE
                    }

                    @Override
                    Player getEffectiveUser() {
                        return PTWO
                    }

                    @Override
                    void setEffectiveUser(final Player player) {

                    }
                }
                , null)

        assert PONE == service.sessionPlayer
    }

    void testSessionPlayerAnnotations() {
        Method m = SecurityService.class.getMethod("getSessionPlayer", [] as Class<?>[])
        assert (m.annotations.size() == 2 ||
                (m.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && m.annotations.size() == 3)
        )
        assert m.isAnnotationPresent(GET.class)
        assert m.isAnnotationPresent(Produces.class)
        assert m.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        def params = m.parameterAnnotations
        assert params.length == 0
    }
}
