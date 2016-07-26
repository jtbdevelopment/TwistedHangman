package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.mongo.dao.AbstractMongoMultiPlayerGameRepository
import groovy.transform.CompileStatic

/**
 * Date: 1/13/15
 * Time: 7:07 AM
 */
@CompileStatic
interface GameRepository extends AbstractMongoMultiPlayerGameRepository<GameFeature, Game> {
}
