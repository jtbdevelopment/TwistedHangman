package com.jtbdevelopment.TwistedHangman.game.utility;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jtbdevelopment.TwistedHangman.dao.PreMadePuzzleRepository;
import com.jtbdevelopment.TwistedHangman.exceptions.system.PreMadePuzzleFinderException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Date: 11/2/14 Time: 4:01 PM
 */
public class RandomPreMadePuzzleFinderTest {

  private PreMadePuzzleRepository repository = mock(PreMadePuzzleRepository.class);
  private RandomCannedGameFinder finder = new RandomCannedGameFinder(repository);
  private PreMadePuzzle game = mock(PreMadePuzzle.class);

  @Test
  public void testReturnsARandomGameFromSingleItemUniverse() {
    Page<PreMadePuzzle> page = mock(Page.class);
    when(page.getContent()).thenReturn(Collections.singletonList(game));

    when(repository.count()).thenReturn(1L);
    when(repository.findAll(PageRequest.of(0, 1))).thenReturn(page);
    Assert.assertSame(game, finder.getRandomGame());
  }

  @Test(expected = PreMadePuzzleFinderException.class)
  public void testExceptionIfNoGames() {
    when(repository.count()).thenReturn(0L);
    finder.getRandomGame();
  }

  @Test(expected = PreMadePuzzleFinderException.class)
  public void testExceptionIfNoContent() {
    Page<PreMadePuzzle> page = mock(Page.class);
    when(page.getContent()).thenReturn(Collections.emptyList());

    when(repository.count()).thenReturn(1L);
    when(repository.findAll(PageRequest.of(0, 1))).thenReturn(page);
    finder.getRandomGame();
  }

  @Test(expected = PreMadePuzzleFinderException.class)
  public void testExceptionIfTooMuchContent() {
    Page<PreMadePuzzle> page = mock(Page.class);
    when(page.getContent()).thenReturn(Arrays.asList(game, game));

    when(repository.count()).thenReturn(1L);
    when(repository.findAll(PageRequest.of(0, 1))).thenReturn(page);
    finder.getRandomGame();
  }

  @Test
  public void testReturnsARandomGameFromLargeUniverse() {
    final long top = Long.MAX_VALUE / 2;
    final Page<PreMadePuzzle> page = mock(Page.class);
    when(page.getContent()).thenReturn(Collections.singletonList(game));

    when(repository.count()).thenReturn(top);
    when(repository.findAll(Matchers.isA(PageRequest.class))).then(
        (Answer<Page>) invocation -> {
          PageRequest request = (PageRequest) invocation.getArguments()[0];
          assertEquals(1, request.getPageSize());
          Assert.assertTrue(request.getPageNumber() < top);
          return page;
        });

    Assert.assertSame(game, finder.getRandomGame());
  }

  @Test
  public void testReturnsARandomGameFromWithSource() {
    String source = "Interwebs";

    final long top = Long.MAX_VALUE / 2;
    Page<PreMadePuzzle> page = mock(Page.class);
    when(page.getContent()).thenReturn(Collections.singletonList(game));

    when(repository.countBySource(source)).thenReturn(top);
    when(repository.findBySource(Matchers.eq(source), Matchers.isA(PageRequest.class)))
        .then((Answer<List<PreMadePuzzle>>) invocation -> {
          PageRequest request = (PageRequest) invocation.getArguments()[1];
          assertEquals(1, request.getPageSize());
          Assert.assertTrue(request.getPageNumber() < top);
          return Arrays.asList(game);
        });

    Assert.assertSame(game, finder.getRandomGame(source));
  }
}
