package com.jtbdevelopment.gamecore.players

/**
 * Date: 12/16/14
 * Time: 7:00 AM
 */
class ManualPlayerTest extends GroovyTestCase {

    void testSourceDefaults() {
        ManualPlayer p = new ManualPlayer()

        assert p.source == ManualPlayer.MANUAL_SOURCE
    }

    void testSourceCantChange() {
        ManualPlayer p = new ManualPlayer()

        p.source = "SomethingElse"
        assert p.source == ManualPlayer.MANUAL_SOURCE
    }
}
