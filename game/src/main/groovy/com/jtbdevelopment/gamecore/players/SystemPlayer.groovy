package com.jtbdevelopment.gamecore.players

import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
@CompileStatic
class SystemPlayer extends Player {
    public static final String SYSTEM_ID_DISPLAY_NAME = "TwistedHangman"
    public static final String SYSTEM_ID_SOURCE = "System"
    public static final ObjectId SYSTEM_ID_ID = new ObjectId("000000000000000000000000");
    public static final SystemPlayer SYSTEM_PLAYER = new SystemPlayer(
            id: SYSTEM_ID_ID,
            displayName: SYSTEM_ID_DISPLAY_NAME,
            source: SYSTEM_ID_SOURCE,
            sourceId: SYSTEM_ID_ID.toHexString())
    public static final String SYSTEM_ID_MD5 = SYSTEM_PLAYER.md5
}
