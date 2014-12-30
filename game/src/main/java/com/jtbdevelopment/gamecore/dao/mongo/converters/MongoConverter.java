package com.jtbdevelopment.gamecore.dao.mongo.converters;

import groovy.transform.CompileStatic;
import org.springframework.core.convert.converter.Converter;

/**
 * Date: 11/9/2014
 * Time: 7:09 PM
 * <p>
 * Marker interface
 */
@CompileStatic
public interface MongoConverter<S, T> extends Converter<S, T> {
}
