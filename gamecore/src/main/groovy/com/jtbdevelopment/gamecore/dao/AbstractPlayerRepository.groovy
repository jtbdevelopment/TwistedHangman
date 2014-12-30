package com.jtbdevelopment.gamecore.dao

import com.jtbdevelopment.gamecore.players.PlayerInt
import groovy.transform.CompileStatic
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Date: 12/30/2014
 * Time: 11:07 AM
 */
@CompileStatic
@NoRepositoryBean
interface AbstractPlayerRepository<ID extends Serializable> extends PagingAndSortingRepository<PlayerInt<ID>, ID> {
    List<PlayerInt<ID>> findByMd5In(final Collection<String> md5s);

    PlayerInt<ID> findBySourceAndSourceId(final String source, final String sourceId);

    List<PlayerInt<ID>> findBySourceAndDisabled(final String source, final boolean disabled);
}