package com.jtbdevelopment.TwistedHangman.factory

import com.jtbdevelopment.TwistedHangman.game.state.HangmanGameFeature
import groovy.transform.CompileStatic

/**
 * Date: 11/3/14
 * Time: 9:25 PM
 */
@CompileStatic
public interface FeatureExpander {
    void enhanceFeatureSet(final Set<HangmanGameFeature> features, final List<String> players)
}