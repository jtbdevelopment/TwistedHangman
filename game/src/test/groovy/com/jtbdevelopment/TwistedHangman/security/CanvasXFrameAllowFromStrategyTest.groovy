package com.jtbdevelopment.TwistedHangman.security

/**
 * Date: 1/27/2015
 * Time: 3:44 PM
 */
class CanvasXFrameAllowFromStrategyTest extends GroovyTestCase {
    CanvasXFrameAllowFromStrategy allowFromStrategy = new CanvasXFrameAllowFromStrategy()

    void testGetAllowFromValue() {
        assert allowFromStrategy.getAllowFromValue(null) == "https://apps.facebook.com"
    }
}
