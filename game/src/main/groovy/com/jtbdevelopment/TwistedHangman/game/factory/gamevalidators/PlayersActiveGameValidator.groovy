package com.jtbdevelopment.TwistedHangman.game.factory.gamevalidators

import com.jtbdevelopment.TwistedHangman.game.factory.GameValidator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.players.Player
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/8/14
 * Time: 8:55 AM
 */
@Component
@CompileStatic
class PlayersActiveGameValidator implements GameValidator {
    public static final String ERROR_MESSAGE = "Game contains inactive players."
    @Autowired
    AbstractPlayerRepository playerRepository

    @Override
    boolean validateGame(final Game game) {
        Iterable<Player<ObjectId>> loaded = playerRepository.findAll(game.players.collect { Player<ObjectId> player -> player.id })

        Collection<Player<ObjectId>> all = loaded.findAll {
            Player<ObjectId> player ->
                !player.disabled
        }
        Collection<ObjectId> loadedActivePlayers = all.collect {
            Player<ObjectId> player ->
                player.id
        }
        return loadedActivePlayers.size() == game.players.size()
    }

    @Override
    String errorMessage() {
        return ERROR_MESSAGE
    }
}
