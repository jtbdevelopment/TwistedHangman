package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.players.Player

/**
 * Date: 1/13/15
 * Time: 7:12 AM
 */
interface FeatureExpander {
    public void enhanceFeatureSet(final Set<GameFeature> features, final Collection<Player> players)
}