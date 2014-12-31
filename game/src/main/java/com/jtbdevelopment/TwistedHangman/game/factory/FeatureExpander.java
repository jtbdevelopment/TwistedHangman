package com.jtbdevelopment.TwistedHangman.game.factory;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.games.players.Player;

import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/3/14
 * Time: 9:25 PM
 */
public interface FeatureExpander {
    public void enhanceFeatureSet(final Set<GameFeature> features, final Collection<Player> players);
}

