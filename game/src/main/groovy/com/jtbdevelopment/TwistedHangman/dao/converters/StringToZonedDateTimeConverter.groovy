package com.jtbdevelopment.TwistedHangman.dao.converters

import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Date: 11/9/2014
 * Time: 7:08 PM
 */
@Component
@CompileStatic
class StringToZonedDateTimeConverter implements MongoConverter<String, ZonedDateTime> {
    public static final ZoneId GMT = ZoneId.of("GMT")

    @Override
    ZonedDateTime convert(final String source) {
        return Instant.parse(source).atZone(GMT)
    }
}
