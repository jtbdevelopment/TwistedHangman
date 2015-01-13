package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Date: 1/13/15
 * Time: 7:08 AM
 */
@CompileStatic
interface PreMadePuzzleRepository extends PagingAndSortingRepository<PreMadePuzzle, ObjectId> {
    public abstract long countBySource(final String source)

    public abstract List<PreMadePuzzle> findBySource(final String source, final Pageable page)

    public abstract List<PreMadePuzzle> removeBySource(final String source)
}