package com.jtbdevelopment.TwistedHangman.dao;

import com.jtbdevelopment.TwistedHangman.players.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Date: 11/3/14
 * Time: 6:57 AM
 */
@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player, String> {
    public abstract List<Player> findByMd5In(final Collection<String> md5s);

    public abstract List<Player> findBySourceAndSourceId(final String source, final String sourceId);

    public abstract List<Player> findBySourceAndDisabled(final String source, final boolean disabled);
}
