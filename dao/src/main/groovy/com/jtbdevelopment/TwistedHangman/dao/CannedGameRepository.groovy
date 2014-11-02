package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.CannedGame
import groovy.transform.CompileStatic
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Date: 11/1/14
 * Time: 6:32 PM
 */
@Repository
@CompileStatic
interface CannedGameRepository extends PagingAndSortingRepository<CannedGame, String> {
    long countBySource(final String source)

    List<CannedGame> findBySource(final String source)

    List<CannedGame> findBySource(final String source, final Pageable page)

    List<CannedGame> removeBySource(final String source)
}
