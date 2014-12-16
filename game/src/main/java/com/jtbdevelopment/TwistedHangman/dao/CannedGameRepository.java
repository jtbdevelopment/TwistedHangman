package com.jtbdevelopment.TwistedHangman.dao;

import com.jtbdevelopment.TwistedHangman.game.utility.CannedGame;
import groovy.transform.CompileStatic;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 11/1/14
 * Time: 6:32 PM
 */
@Repository
@CompileStatic
public interface CannedGameRepository extends PagingAndSortingRepository<CannedGame, ObjectId> {
    public abstract long countBySource(final String source);

    public abstract List<CannedGame> findBySource(final String source);

    public abstract List<CannedGame> findBySource(final String source, final Pageable page);

    public abstract List<CannedGame> removeBySource(final String source);
}
