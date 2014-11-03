package com.jtbdevelopment.TwistedHangman.game

import groovy.transform.CompileStatic
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
    String id

    @Indexed
    String source

    @Indexed
    String category
    String wordPhrase
}
