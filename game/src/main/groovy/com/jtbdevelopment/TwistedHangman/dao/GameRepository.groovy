package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import com.jtbdevelopment.games.mongo.dao.AbstractMongoMultiPlayerGameRepository
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable

import java.time.ZonedDateTime

/**
 * Date: 1/13/15
 * Time: 7:07 AM
 */
@CompileStatic
interface GameRepository extends AbstractMongoMultiPlayerGameRepository<GameFeature, Game> {
    //  TODO - move to core - see comment there
    //  TODO - also move PlayerGamesFinderHandler then
    List<Game> findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan(
            final ObjectId id, final GamePhase gamePhase, final ZonedDateTime cutoff, final Pageable pageable)
}
