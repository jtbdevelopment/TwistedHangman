package com.jtbdevelopment.TwistedHangman.rest.services

import com.jtbdevelopment.TwistedHangman.TwistedHangmanTestCase
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.players.PlayerRoles
import com.jtbdevelopment.games.security.SessionUserInfo
import groovy.transform.TypeChecked
import org.bson.types.ObjectId
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl

import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.lang.reflect.Method

/**
 * Date: 12/24/14
 * Time: 2:23 PM
 */
class AdminServicesTest extends TwistedHangmanTestCase {
    AdminServices adminServices = new AdminServices()

    void testClassAnnotations() {
        assert AdminServices.class.isAnnotationPresent(RolesAllowed.class)
        assert AdminServices.class.getAnnotation(RolesAllowed.class).value() == [PlayerRoles.ADMIN]
    }

    void testPlayersToSimulateNoParams() {
        def repo = [
                findAll: {
                    PageRequest pageRequest ->
                        assert pageRequest.pageNumber == AdminServices.DEFAULT_PAGE
                        assert pageRequest.pageSize == AdminServices.DEFAULT_PAGE_SIZE
                        assert pageRequest.sort.properties.size() == 1
                        assert pageRequest.sort.getOrderFor("displayName").direction == Sort.Direction.ASC
                        new PageImpl<MongoPlayer>([PTWO, PTHREE])
                }
        ] as AbstractPlayerRepository<ObjectId>
        adminServices.playerRepository = repo;

        assert adminServices.playersToSimulate(null, null) == [PTWO, PTHREE] as Set
    }

    void testPlayersToSimulateAnnotations() {
        Method m = AdminServices.class.getMethod("playersToSimulate", [Integer.class, Integer.class] as Class<?>[])
        assert (m.annotations.size() == 2 ||
                (m.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && m.annotations.size() == 3)
        )
        assert m.isAnnotationPresent(GET.class)
        assert m.isAnnotationPresent(Produces.class)
        assert m.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        def params = m.parameterAnnotations
        assert params.length == 2
        assert params[0].length == 1
        assert params[0][0].annotationType() == QueryParam.class
        assert ((QueryParam) params[0][0]).value() == "page"
        assert params[1].length == 1
        assert params[1][0].annotationType() == QueryParam.class
        assert ((QueryParam) params[1][0]).value() == "pageSize"
    }

    void testSwitchEffectiveUser() {
        SecurityContextHolder.context = new SecurityContextImpl()
        def session = new SessionUserInfo<ObjectId>() {
            Player<ObjectId> sessionUser = PONE
            Player<ObjectId> effectiveUser = PONE

            @Override
            Player<ObjectId> getSessionUser() {
                return sessionUser
            }

            @Override
            Player<ObjectId> getEffectiveUser() {
                return effectiveUser
            }

            @Override
            void setEffectiveUser(final Player<ObjectId> player) {
                this.effectiveUser = player
            }
        }
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(session, null)
        SecurityContextHolder.context.authentication = authenticationToken

        def repo = [
                findOne: {
                    ObjectId id ->
                        assert id == PTWO.id
                        return PTWO
                }
        ] as AbstractPlayerRepository<ObjectId>

        adminServices.playerRepository = repo

        assert PTWO.is(adminServices.switchEffectiveUser(PTWO.id.toHexString()))
        assert session.effectiveUser == PTWO
        assert session.sessionUser == PONE
    }

    void testSwitchEffectiveUserBadID() {
        SecurityContextHolder.context = new SecurityContextImpl()
        def session = new SessionUserInfo<ObjectId>() {
            Player<ObjectId> sessionUser = PONE
            Player<ObjectId> effectiveUser = PONE

            @Override
            Player<ObjectId> getSessionUser() {
                return sessionUser
            }

            @Override
            Player<ObjectId> getEffectiveUser() {
                return effectiveUser
            }

            @Override
            void setEffectiveUser(final Player<ObjectId> player) {
                this.effectiveUser = player
            }
        }
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(session, null)
        SecurityContextHolder.context.authentication = authenticationToken

        def repo = [
                findOne: {
                    ObjectId id ->
                        assert id == PTWO.id
                        return null
                }
        ] as AbstractPlayerRepository<ObjectId>

        adminServices.playerRepository = repo


        def response = adminServices.switchEffectiveUser(PTWO.id.toHexString())
        assert response in Response
        assert response.status == Response.Status.NOT_FOUND.statusCode
        assert response.mediaType == MediaType.TEXT_PLAIN_TYPE
        assert session.effectiveUser == PONE
        assert session.sessionUser == PONE
    }

    void testSwitchAnnotations() {
        Method m = AdminServices.class.getMethod("switchEffectiveUser", [String.class] as Class<?>[])
        assert (m.annotations.size() == 3 ||
                (m.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && m.annotations.size() == 4)
        )
        assert m.isAnnotationPresent(PUT.class)
        assert m.isAnnotationPresent(Produces.class)
        assert m.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assert m.isAnnotationPresent(Path.class)
        assert m.getAnnotation(Path.class).value() == "{playerID}"
        def params = m.parameterAnnotations
        assert params.length == 1
        assert params[0].length == 1
        assert params[0][0].annotationType() == PathParam.class
        assert ((PathParam) params[0][0]).value() == "playerID"
    }

}
