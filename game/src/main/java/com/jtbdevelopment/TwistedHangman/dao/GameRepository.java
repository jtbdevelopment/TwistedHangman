package com.jtbdevelopment.TwistedHangman.dao;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GamePhase;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Date: 11/3/14
 * Time: 6:58 AM
 * <p>
 * TODO - cache
 */
public interface GameRepository extends PagingAndSortingRepository<Game, ObjectId> {
    public abstract List<Game> findByPlayersId(final ObjectId id);

    public abstract List<Game> findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan(final ObjectId id, final GamePhase gamePhase, final ZonedDateTime cutoff, final Pageable pageable);
}
