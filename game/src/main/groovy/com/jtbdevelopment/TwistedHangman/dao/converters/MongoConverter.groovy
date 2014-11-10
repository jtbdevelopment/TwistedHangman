package com.jtbdevelopment.TwistedHangman.dao.converters

import groovy.transform.CompileStatic
import org.springframework.core.convert.converter.Converter

/**
 * Date: 11/9/2014
 * Time: 7:09 PM
 */
@CompileStatic
interface MongoConverter<S, T> extends Converter<S, T> {

}