package com.jtbdevelopment.TwistedHangman.rest.aop

import groovy.transform.CompileStatic
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

/**
 * Date: 11/12/14
 * Time: 7:06 PM
 */
@Aspect
@CompileStatic
@Component
class RestSystemArchitecture {
    RestSystemArchitecture() {
    }

    @Pointcut("within(com.jtbdevelopment.TwistedHangman.rest.services..*)")
    public void inRestServices() {}
}
