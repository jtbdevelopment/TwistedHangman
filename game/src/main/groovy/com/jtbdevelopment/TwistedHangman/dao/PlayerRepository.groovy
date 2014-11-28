package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.players.Player
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Date: 11/3/14
 * Time: 6:57 AM
 */
@Repository
interface PlayerRepository extends PagingAndSortingRepository<Player, String> {
    List<Player> findByMd5In(final Collection<String> md5s)

    List<Player> findBySourceAndDisabled(final String source, final boolean disabled);
}
