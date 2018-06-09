package com.jtbdevelopment.TwistedHangman.rest.services;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.dao.StringToIDConverter;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.services.AbstractAdminServices;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/27/2014 Time: 6:34 PM
 */
@SuppressWarnings("unused")
@Component
public class AdminServices extends
    AbstractAdminServices<ObjectId, GameFeature, THGame, MongoPlayer> {

  public AdminServices(
      AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      AbstractGameRepository<ObjectId, GameFeature, THGame> gameRepository,
      StringToIDConverter<ObjectId> stringToIDConverter) {
    super(playerRepository, gameRepository, stringToIDConverter);
  }
}
