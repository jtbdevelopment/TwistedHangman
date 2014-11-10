package com.jtbdevelopment.TwistedHangman.game.handlers

import com.jtbdevelopment.TwistedHangman.dictionary.Validator
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.players.Player
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 11/9/2014
 * Time: 9:18 PM
 *
 * param should be map of 2 keys
 */
@CompileStatic
@Component
class SetPuzzleHandler extends AbstractGameActionHandler<Map<String, String>> {
    public static final String CATEGORY_KEY = "CATEGORY"
    public static final String WORDPHRASE_KEY = "WORDPHRASE"

    @Autowired
    Validator validator

    @Override
    protected Game handleActionInternal(final Player player, final Game game, final Map<String, String> param) {

        return null
    }
}
