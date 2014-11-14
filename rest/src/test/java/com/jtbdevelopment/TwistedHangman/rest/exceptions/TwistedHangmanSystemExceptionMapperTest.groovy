package com.jtbdevelopment.TwistedHangman.rest.exceptions

import com.jtbdevelopment.TwistedHangman.exceptions.TwistedHangmanSystemException

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Date: 11/13/14
 * Time: 7:09 AM
 */
class TwistedHangmanSystemExceptionMapperTest extends GroovyTestCase {
    TwistedHangmanSystemExceptionMapper mapper = new TwistedHangmanSystemExceptionMapper()

    void testToResponse() {
        String s = "A MESSAGE"
        Response response = mapper.toResponse(new TwistedHangmanSystemException(s) {})
        assert response.entity == s
        assert response.status == Response.Status.NOT_FOUND.statusCode
        assert response.mediaType == MediaType.TEXT_PLAIN_TYPE
    }
}
