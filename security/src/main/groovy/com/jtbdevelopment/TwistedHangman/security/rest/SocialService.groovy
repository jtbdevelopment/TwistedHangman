package com.jtbdevelopment.TwistedHangman.security.rest

import com.jtbdevelopment.TwistedHangman.security.facebook.FacebookProperties
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 12/25/14
 * Time: 9:10 PM
 */
@Path("/signin/apis")
@Component
@CompileStatic
class SocialService {
    @Autowired(required = false)
    FacebookProperties facebookProperties

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, String> apiInfo() {
        def apis = [:]
        if (facebookProperties && !StringUtils.isEmpty(facebookProperties.clientID)) {
            apis["facebookAppId"] = facebookProperties.clientID
        }
        apis
    }
}
