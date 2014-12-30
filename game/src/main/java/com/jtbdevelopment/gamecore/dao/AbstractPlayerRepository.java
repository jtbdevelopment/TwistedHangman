package com.jtbdevelopment.gamecore.dao;

import com.jtbdevelopment.gamecore.players.PlayerInt;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Date: 11/3/14
 * Time: 6:57 AM
 * <p>
 * TODO - cache
 */
@NoRepositoryBean
public interface AbstractPlayerRepository<ID extends Serializable> extends PagingAndSortingRepository<PlayerInt<ID>, ID> {
    public List<PlayerInt<ID>> findByMd5In(final Collection<String> md5s);

    public PlayerInt<ID> findBySourceAndSourceId(final String source, final String sourceId);

    public List<PlayerInt<ID>> findBySourceAndDisabled(final String source, final boolean disabled);
}
