package com.jtbdevelopment.TwistedHangman

import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 12/20/2014
 * Time: 4:12 PM
 *
 * Useful sanity check to verify if non-authenticated rest is working
 */
@Path("test")
@Component
@CompileStatic
class TestService {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String get() {
        return "no auth test"
    }
}
