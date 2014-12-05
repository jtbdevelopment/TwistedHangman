package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import java.time.ZonedDateTime

/**
 * Date: 11/3/14
 * Time: 6:58 AM
 */
public interface GameRepository extends PagingAndSortingRepository<Game, String> {
    List<Game> findByPlayersId(final String id)

    List<Game> findByPlayersIdAndGamePhase(final String id, final GamePhase gamePhase, final Pageable pageable)

    List<Game> findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan(
            final String id,
            final GamePhase gamePhase,
            final ZonedDateTime cutoff,
            final Pageable pageable);
}