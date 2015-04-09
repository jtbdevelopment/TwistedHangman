package com.jtbdevelopment.TwistedHangman.game.factory

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.players.Player

/**
 * Date: 1/13/15
 * Time: 7:12 AM
 *
 * Note - having this seemed to a mistake later on - did not move to core
 */
interface FeatureExpander {
     void enhanceFeatureSet(final Set<GameFeature> features, final Collection<Player> players)
}