package com.jtbdevelopment.TwistedHangman.players

import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.players.PlayerPayLevel
import com.jtbdevelopment.games.publish.PlayerPublisher
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

import static com.jtbdevelopment.games.dao.caching.CacheConstants.*

/**
 * Date: 2/4/15
 * Time: 7:02 PM
 */
@Component
@CompileStatic
class PlayerGameTracker {
    private static final Update UPDATE_FREE_GAMES = new Update().inc(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD, 1)
    private static
    final Update REVERT_FREE_GAMES = new Update().inc(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD, -1)
    private static
    final Update UPDATE_PAID_GAMES = new Update().inc(TwistedHangmanPlayerAttributes.PAID_GAMES_FIELD, -1)
    private static final Update REVERT_PAID_GAMES = new Update().inc(TwistedHangmanPlayerAttributes.PAID_GAMES_FIELD, 1)
    private static final FindAndModifyOptions RETURN_NEW_OPTION = new FindAndModifyOptions().returnNew(true)

    @Autowired
    MongoOperations mongoOperations

    @Autowired
    PlayerPublisher playerPublisher

    public enum GameEligibility {
        FreeGameUsed,
        PaidGameUsed,
        NoGamesAvailable
    }

    public static class GameEligibilityResult {
        GameEligibility eligibility
        Player player
    }

    /**
     * Checks eligibility for player and decrements appropriate value
     * Caller should retain value until all actions completed in case rollback is needed
     *
     * @param player
     * @return result that can be checked if player is eligible and to pass in case reverting needed
     */
    @Caching(
            evict = [
                    @CacheEvict(value = PLAYER_ID_CACHE, key = '#result.player.id'),
                    @CacheEvict(value = PLAYER_MD5_CACHE, key = '#result.player.md5'),
                    @CacheEvict(value = PLAYER_S_AND_SID_CACHE, key = '#result.player.sourceAndSourceId')
            ]
    )
    GameEligibilityResult getGameEligibility(final Player player) {
        int freeGames = (player.payLevel == PlayerPayLevel.FreeToPlay ? TwistedHangmanPlayerAttributes.DEFAULT_FREE_GAMES_PER_DAY : TwistedHangmanPlayerAttributes.DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY)

        //  Try free game first
        MongoPlayer updated = (MongoPlayer) mongoOperations.findAndModify(
                Query.query(Criteria.where('_id').is(player.id).and(TwistedHangmanPlayerAttributes.FREE_GAMES_FIELD).lt(freeGames)),
                UPDATE_FREE_GAMES,
                RETURN_NEW_OPTION,
                player.class
        )
        //  TODO - verify if quantity compare necessary and test if so
        if (updated != null &&
                ((TwistedHangmanPlayerAttributes) updated.gameSpecificPlayerAttributes).freeGamesUsedToday != ((TwistedHangmanPlayerAttributes) player.gameSpecificPlayerAttributes).freeGamesUsedToday) {
            playerPublisher.publish(updated)
            return new GameEligibilityResult(eligibility: GameEligibility.FreeGameUsed, player: updated)
        }

        updated = (MongoPlayer) mongoOperations.findAndModify(
                Query.query(Criteria.where('_id').is(player.id).and(TwistedHangmanPlayerAttributes.PAID_GAMES_FIELD).gt(0)),
                UPDATE_PAID_GAMES,
                RETURN_NEW_OPTION,
                player.class
        )
        //  TODO - verify if quantity compare necessary and test if so
        if (updated != null &&
                ((TwistedHangmanPlayerAttributes) updated.gameSpecificPlayerAttributes).availablePurchasedGames != ((TwistedHangmanPlayerAttributes) player.gameSpecificPlayerAttributes).availablePurchasedGames) {
            playerPublisher.publish(updated)
            return new GameEligibilityResult(eligibility: GameEligibility.PaidGameUsed, player: updated)
        }


        return new GameEligibilityResult(eligibility: GameEligibility.NoGamesAvailable, player: player)
    }

    /**
     * Revert the result of getGameEligibility
     *
     * @param gameEligibilityResult returned from getGameEligibility
     */
    @Caching(
            evict = [
                    @CacheEvict(value = PLAYER_ID_CACHE, key = '#p0.player.id'),
                    @CacheEvict(value = PLAYER_MD5_CACHE, key = '#p0.player.md5'),
                    @CacheEvict(value = PLAYER_S_AND_SID_CACHE, key = '#p0.player.sourceAndSourceId')
            ]
    )
    void revertGameEligibility(final GameEligibilityResult gameEligibilityResult) {
        Update revertToUse;
        switch (gameEligibilityResult.eligibility) {
            case GameEligibility.FreeGameUsed:
                revertToUse = REVERT_FREE_GAMES
                break;
            case GameEligibility.PaidGameUsed:
                revertToUse = REVERT_PAID_GAMES
                break;
        }

        if (revertToUse) {
            MongoPlayer updated = (MongoPlayer) mongoOperations.findAndModify(
                    Query.query(Criteria.where('_id').is(gameEligibilityResult.player.id)),
                    revertToUse,
                    RETURN_NEW_OPTION,
                    gameEligibilityResult.player.class
            )
            if (updated) {
                playerPublisher.publish(updated)
            }
        }
    }
}
