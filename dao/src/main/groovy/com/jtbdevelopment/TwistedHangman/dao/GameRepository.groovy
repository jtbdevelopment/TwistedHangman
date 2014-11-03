package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.game.Game
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Date: 11/3/14
 * Time: 6:58 AM
 */
public interface GameRepository extends PagingAndSortingRepository<Game, String> {

}