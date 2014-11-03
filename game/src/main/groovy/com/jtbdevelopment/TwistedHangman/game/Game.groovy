package com.jtbdevelopment.TwistedHangman.game

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.ZonedDateTime

/**
 * Date: 11/2/2014
 * Time: 9:36 PM
 */
@Document
abstract class Game {
    public enum GamePhase {
        Challenge,  /*  Agreement from initial players  */
        Setup, /*  Setting word phrases  */
        Playing,
        RematchOption,  /*  Option to continue to a new game  */
        Closed,
        Declined,  /*  Challenge was rejected  */
    }

    @Id
    String id
    @Version
    int version

    String previousId
    String remathId

    GamePhase gamePhase

    @Indexed
    ZonedDateTime created
    ZonedDateTime declined
    @Indexed
    ZonedDateTime lastMove
    @Indexed
    ZonedDateTime completed
    @Indexed
    ZonedDateTime rematched
}
