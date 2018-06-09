package com.jtbdevelopment.TwistedHangman.rest.services;

import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.services.AbstractPlayerGatewayService;
import com.jtbdevelopment.games.rest.services.AbstractPlayerServices;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/14/14 Time: 6:36 AM
 */
@Path("/")
@Component
public class PlayerGatewayService extends
    AbstractPlayerGatewayService<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> {

  PlayerGatewayService(
      final AbstractPlayerServices<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> playerServices) {
    super(playerServices);
  }

  @GET
  @Path("features")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<GameFeature, String> featuresAndDescriptions() {
    return Arrays.stream(GameFeature.values())
        .filter(f -> !f.getInternal())
        .collect(Collectors.toMap(f -> f, GameFeature::getDescription));
  }

}
