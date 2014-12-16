package com.jtbdevelopment.TwistedHangman.game.utility

import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 11/1/14
 * Time: 5:54 PM
 */
@Document
@CompileStatic
class CannedGame {
    @Id
    ObjectId id

    @Indexed
    String source

    @Indexed
    String category
    String wordPhrase
}
