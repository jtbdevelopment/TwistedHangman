'use strict';

describe('Service: showGameSevice', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var showGameServiceGame = {
    game: {
      id: 'id2',
      players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
      puzzleSetter: 'md4',
      solverStates: {
        md1: {isGameOver: true, isGameWon: true},
        md2: {isGameOver: true, isGameWon: false},
        md3: {isGameOver: false, isGameWon: false}
      },
      playerScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5}
    },
    gameID: 'id'
  };

  var service, cache, rootscope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $injector, twShowGameCache) {
    cache = twShowGameCache;
    rootscope = $rootScope;
    spyOn($rootScope, '$broadcast');
    service = $injector.get('twShowGameService');
  }));

  it('initialize scope', function () {
    var scope = rootscope.$new();

    scope.game = showGameServiceGame;
    service.initializeScope(scope);
    expect(cache.get('scope')).toBe(scope);
    expect(scope.workingWordPhraseArray).toEqual([]);
    expect(scope.workingWordPhraseClasses).toEqual([]);
    expect(scope.letters).toEqual(['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']);
    expect(scope.letterClasses).toEqual(['regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular']);
  });
});

