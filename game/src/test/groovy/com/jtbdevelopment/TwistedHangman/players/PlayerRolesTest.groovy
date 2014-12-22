package com.jtbdevelopment.TwistedHangman.players

/**
 * Date: 12/22/14
 * Time: 12:19 PM
 */
class PlayerRolesTest extends GroovyTestCase {
    void testPlayer() {
        assert PlayerRoles.PLAYER == 'Player'
    }

    void testAdmin() {
        assert PlayerRoles.ADMIN == 'Admin'
    }
}
