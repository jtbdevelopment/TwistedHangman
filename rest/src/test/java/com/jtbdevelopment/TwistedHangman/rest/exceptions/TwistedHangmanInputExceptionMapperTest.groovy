package com.jtbdevelopment.TwistedHangman.rest.exceptions

import com.jtbdevelopment.gamecore.exceptions.GameInputException

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/13/14
 * Time: 7:09 AM
 */
class TwistedHangmanInputExceptionMapperTest extends GroovyTestCase {
    TwistedHangmanInputExceptionMapper mapper = new TwistedHangmanInputExceptionMapper()

    void testToResponse() {
        String s = "A MESSAGE"
        Response response = mapper.toResponse(new GameInputException(s) {})
        assert response.entity == s
        assert response.status == Response.Status.BAD_REQUEST.statusCode
        assert response.mediaType == MediaType.TEXT_PLAIN_TYPE
    }
}
