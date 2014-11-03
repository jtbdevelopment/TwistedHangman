package com.jtbdevelopment.TwistedHangman.players

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
@Document
class Player {
    public static final String SYSTEM_ID_DISPLAY_NAME = "TwistedHangman"
    public static final String SYSTEM_ID_SOURCE = "System"
    public static final String SYSTEM_ID_ID = "0"

    @Id
    String id
    String source
    String displayName
}
