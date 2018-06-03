package com.jtbdevelopment.TwistedHangman.dao;

import com.jtbdevelopment.TwistedHangman.game.utility.PreMadePuzzle;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Date: 1/13/15 Time: 7:08 AM
 */
public interface PreMadePuzzleRepository extends
    PagingAndSortingRepository<PreMadePuzzle, ObjectId> {

      long countBySource(final String source);

      List<PreMadePuzzle> findBySource(final String source, final Pageable page);

      List<PreMadePuzzle> removeBySource(final String source);
}
