package com.jtbdevelopment.TwistedHangman.factory;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;

import java.util.List;
import java.util.Set;

/**
 * Date: 11/3/14
 * Time: 9:25 PM
 */
public interface FeatureExpander {
    public abstract void enhanceFeatureSet(final Set<GameFeature> features, final List<String> players);
}
