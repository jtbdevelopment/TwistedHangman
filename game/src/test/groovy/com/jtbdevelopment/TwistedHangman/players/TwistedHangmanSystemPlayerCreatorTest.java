package com.jtbdevelopment.TwistedHangman.players;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import com.jtbdevelopment.games.mongo.dao.MongoPlayerRepository;
import com.jtbdevelopment.games.mongo.players.MongoPlayerFactory;
import com.jtbdevelopment.games.mongo.players.MongoSystemPlayer;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * Date: 1/13/15 Time: 6:59 AM
 */
public class TwistedHangmanSystemPlayerCreatorTest {

  private MongoPlayerRepository playerRepository = Mockito.mock(MongoPlayerRepository.class);
  private MongoPlayerFactory playerFactory = Mockito.mock(MongoPlayerFactory.class);
  private TwistedHangmanSystemPlayerCreator systemPlayer = new TwistedHangmanSystemPlayerCreator(
      playerRepository, playerFactory);

  @Test
  public void testLoadsSystemPlayerIfExists() {
    String md5 = "XA135";
    MongoSystemPlayer p = Mockito.mock(MongoSystemPlayer.class);
    when(p.getMd5()).thenReturn(md5);
    when(playerRepository.findById(TwistedHangmanSystemPlayerCreator.TH_ID))
        .thenReturn(Optional.of(p));

    systemPlayer.loadOrCreateSystemPlayers();
    assertSame(p, TwistedHangmanSystemPlayerCreator.TH_PLAYER);
    assertEquals(md5, TwistedHangmanSystemPlayerCreator.TH_MD5);
  }

  @Test
  public void testCreatesIfMissing() {
    String md5 = "XA135";
    final MongoSystemPlayer p = Mockito.mock(MongoSystemPlayer.class);
    when(p.getMd5()).thenReturn(md5);
    when(playerRepository.findById(TwistedHangmanSystemPlayerCreator.TH_ID))
        .thenReturn(Optional.empty());
    when(playerRepository.save(Matchers.isA(MongoSystemPlayer.class)))
        .then(invocation -> {
          MongoSystemPlayer save = (MongoSystemPlayer) invocation.getArguments()[0];
          Assert.assertFalse(save.isAdminUser());
          Assert.assertFalse(save.isDisabled());
          assertEquals(TwistedHangmanSystemPlayerCreator.TH_DISPLAY_NAME, save.getDisplayName());
          assertEquals(TwistedHangmanSystemPlayerCreator.TH_ID, save.getId());
          assertEquals(TwistedHangmanSystemPlayerCreator.TH_ID.toHexString(), save.getSourceId());
          return p;
        });
    MongoSystemPlayer newSystemPlayer = new MongoSystemPlayer();
    when(playerFactory.newSystemPlayer()).thenReturn(newSystemPlayer);

    systemPlayer.loadOrCreateSystemPlayers();

    assertSame(p, TwistedHangmanSystemPlayerCreator.TH_PLAYER);
    assertEquals(md5, TwistedHangmanSystemPlayerCreator.TH_MD5);
  }
}
