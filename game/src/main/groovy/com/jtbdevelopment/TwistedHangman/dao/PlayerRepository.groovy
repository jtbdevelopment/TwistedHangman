package com.jtbdevelopment.TwistedHangman.dao

import com.jtbdevelopment.TwistedHangman.players.Player
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Date: 11/3/14
 * Time: 6:57 AM
 */
@Repository
interface PlayerRepository extends CrudRepository<Player, String> {
}
