package com.jtbdevelopment.gamecore.dao;

import com.jtbdevelopment.TwistedHangman.players.Player;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Date: 11/3/14
 * Time: 6:57 AM
 *
 * TODO - cache
 *
 */
@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player, ObjectId> {
    public List<Player> findByMd5In(final Collection<String> md5s);

    public Player findBySourceAndSourceId(final String source, final String sourceId);

    public List<Player> findBySourceAndDisabled(final String source, final boolean disabled);
}
