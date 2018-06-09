package com.jtbdevelopment.TwistedHangman.rest.services;

import com.jtbdevelopment.TwistedHangman.game.handlers.GuessLetterHandler;
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler;
import com.jtbdevelopment.TwistedHangman.game.handlers.SetPuzzleHandler.CategoryAndWordPhrase;
import com.jtbdevelopment.TwistedHangman.game.handlers.StealLetterHandler;
import com.jtbdevelopment.TwistedHangman.game.state.GameFeature;
import com.jtbdevelopment.TwistedHangman.game.state.THGame;
import com.jtbdevelopment.TwistedHangman.game.state.masking.THMaskedGame;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.AbstractMultiPlayerGameServices;
import com.jtbdevelopment.games.rest.handlers.ChallengeResponseHandler;
import com.jtbdevelopment.games.rest.handlers.ChallengeToRematchHandler;
import com.jtbdevelopment.games.rest.handlers.DeclineRematchOptionHandler;
import com.jtbdevelopment.games.rest.handlers.GameGetterHandler;
import com.jtbdevelopment.games.rest.handlers.QuitHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Date: 11/11/14 Time: 9:42 PM
 */
@Component
public class GameServices extends
    AbstractMultiPlayerGameServices<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> {

  private final StealLetterHandler stealLetterHandler;
  private final GuessLetterHandler guessLetterHandler;
  private final SetPuzzleHandler puzzleHandler;

  GameServices(
      final GameGetterHandler<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> gameGetterHandler,
      final DeclineRematchOptionHandler<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> declineRematchOptionHandler,
      final ChallengeResponseHandler<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> responseHandler,
      final ChallengeToRematchHandler<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> rematchHandler,
      final QuitHandler<ObjectId, GameFeature, THGame, THMaskedGame, MongoPlayer> quitHandler,
      final StealLetterHandler stealLetterHandler, final GuessLetterHandler guessLetterHandler,
      final SetPuzzleHandler setPuzzleHandler) {
    super(gameGetterHandler, declineRematchOptionHandler, responseHandler, rematchHandler,
        quitHandler);
    this.stealLetterHandler = stealLetterHandler;
    this.guessLetterHandler = guessLetterHandler;
    this.puzzleHandler = setPuzzleHandler;
  }

  @PUT
  @Path("puzzle")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Object setPuzzle(CategoryAndWordPhrase categoryAndWordPhrase) {
    return puzzleHandler
        .handleAction(getPlayerID().get(), getGameID().get(), categoryAndWordPhrase);
  }

  @PUT
  @Path("steal/{position}")
  @Produces(MediaType.APPLICATION_JSON)
  public Object stealLetter(@PathParam("position") final int position) {
    return stealLetterHandler.handleAction(getPlayerID().get(), getGameID().get(), position);
  }

  @PUT
  @Path("guess/{letter}")
  @Produces(MediaType.APPLICATION_JSON)
  public Object guessLetter(@PathParam("letter") final String letter) {
    String validated = letter;
    if (StringUtils.isEmpty(letter) || StringUtils.isEmpty(letter.trim())) {
      validated = " ";
    }

    return guessLetterHandler
        .handleAction(getPlayerID().get(), getGameID().get(), validated.charAt(0));
  }
}
