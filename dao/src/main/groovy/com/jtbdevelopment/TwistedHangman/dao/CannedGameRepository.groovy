package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.CannedGame
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Date: 11/1/14
 * Time: 6:32 PM
 */
@Repository
interface CannedGameRepository extends PagingAndSortingRepository<CannedGame, String> {
    List<CannedGame> findBySource(final String source)

    List<CannedGame> removeBySource(final String source)
}
