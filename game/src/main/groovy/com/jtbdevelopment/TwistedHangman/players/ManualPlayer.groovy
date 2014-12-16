package com.jtbdevelopment.TwistedHangman.players

import groovy.transform.CompileStatic
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 12/16/14
 * Time: 6:49 AM
 *
 * For direct login users
 *
 * Username goes in sourceId which should be an email address
 * displayName might default to display name
 *
 */
@Document(collection = "player")
@CompileStatic
class ManualPlayer extends Player {
    public static final String MANUAL_SOURCE = "MANUAL"

    //  TODO - Encrypted + salted password
    String password

    boolean verified = false
    String verificationToken = ""

    public ManualPlayer() {
        super.source = MANUAL_SOURCE
    }

    @Override
    void setSource(final String source) {
    }


}
