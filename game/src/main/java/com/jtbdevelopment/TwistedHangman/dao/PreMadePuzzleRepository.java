package com.jtbdevelopment.TwistedHangman.dao;

import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 11/1/14
 * Time: 6:32 PM
 * <p>
 * TODO - cache
 */
@Repository
public interface PreMadePuzzleRepository extends PagingAndSortingRepository<PreMadePuzzle, ObjectId> {
    public abstract long countBySource(final String source);

    public abstract List<PreMadePuzzle> findBySource(final String source, final Pageable page);

    public abstract List<PreMadePuzzle> removeBySource(final String source);
}
