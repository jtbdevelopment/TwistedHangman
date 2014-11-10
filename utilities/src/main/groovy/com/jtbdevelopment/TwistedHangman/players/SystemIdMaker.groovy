package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.TwistedHangman.dao.GameRepository
import com.jtbdevelopment.TwistedHangman.dao.PlayerRepository
import com.jtbdevelopment.TwistedHangman.game.state.Game
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Date: 11/3/14
 * Time: 7:02 AM
 */
class SystemIdMaker {

    public static void main(final String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-game.xml");
        ctx.refresh();

        PlayerRepository repository = ctx.getBean(PlayerRepository.class)

        Player player = repository.findOne(Player.SYSTEM_ID_ID)
        if (player == null) {
            println "Making system id"
            repository.save(Player.SYSTEM_PLAYER)
        } else {
            println "Verifying system id"
            assert player.source == Player.SYSTEM_ID_SOURCE
            assert player.displayName == Player.SYSTEM_ID_DISPLAY_NAME
        }

        Game game = new Game();
        GameRepository gameRepository = ctx.getBean(GameRepository.class)
        game = gameRepository.save(game)
//        game.created.
//        Thread.sleep(10000)
        game.features.add(GameFeature.SingleWinner)
        game.players.add(repository.findOne(Player.SYSTEM_ID_ID))
        game = gameRepository.save(game)

        game = gameRepository.findOne(game.id)
        println "Complete"
    }
}
