package com.jtbdevelopment.TwistedHangman;

import com.jtbdevelopment.TwistedHangman.game.state.Game;
import com.jtbdevelopment.TwistedHangman.players.TwistedHangmanSystemPlayerCreator;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer;
import org.bson.types.ObjectId;

/**
 * Date: 11/8/14 Time: 9:09 AM
 */
public abstract class TwistedHangmanTestCase extends MongoGameCoreTestCase {

  {
    TwistedHangmanSystemPlayerCreator.TH_PLAYER = new MongoSystemPlayer() {{
      setId(TwistedHangmanSystemPlayerCreator.TH_ID);
      setDisplayName(TwistedHangmanSystemPlayerCreator.TH_DISPLAY_NAME);
      setSourceId(TwistedHangmanSystemPlayerCreator.TH_ID.toHexString());
    }};
    TwistedHangmanSystemPlayerCreator.TH_MD5 = TwistedHangmanSystemPlayerCreator.TH_PLAYER.getMd5();
  }

  protected static Game makeSimpleGame() {
    Game game = new Game();

    game.setId(new ObjectId());
    return game;
  }
}
