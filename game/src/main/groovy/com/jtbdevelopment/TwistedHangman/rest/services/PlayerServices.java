package com.jtbdevelopment.TwistedHangman.rest.services;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.dao.StringToIDConverter;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.AbstractMultiPlayerServices;
import com.jtbdevelopment.games.rest.handlers.NewGameHandler;
import com.jtbdevelopment.games.rest.handlers.PlayerGamesFinderHandler;
import com.jtbdevelopment.games.rest.services.AbstractAdminServices;
import com.jtbdevelopment.games.rest.services.AbstractGameServices;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/14/14 Time: 6:40 AM
 */
@Component
public class PlayerServices extends
    AbstractMultiPlayerServices<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> {

  private final NewGameHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> newGameHandler;

  protected PlayerServices(
      final AbstractGameServices<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> gamePlayServices,
      final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractAdminServices<ObjectId, GameFeature, Game, MongoPlayer> adminServices,
      final StringToIDConverter<ObjectId> stringToIDConverter,
      final PlayerGamesFinderHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> playerGamesFinderHandler,
      final NewGameHandler<ObjectId, GameFeature, Game, MaskedGame, MongoPlayer> newGameHandler) {
    super(gamePlayServices, playerRepository, adminServices, stringToIDConverter,
        playerGamesFinderHandler);
    this.newGameHandler = newGameHandler;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("new")
  public Object createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
    return newGameHandler.handleCreateNewGame(
        getPlayerID().get(),
        featuresAndPlayers.getPlayers(),
        featuresAndPlayers.getFeatures());
  }

  public static class FeaturesAndPlayers {

    private List<String> players;
    private Set<GameFeature> features;

    public List<String> getPlayers() {
      return players;
    }

    public void setPlayers(List<String> players) {
      this.players = players;
    }

    public Set<GameFeature> getFeatures() {
      return features;
    }

    public void setFeatures(Set<GameFeature> features) {
      this.features = features;
    }
  }
}
