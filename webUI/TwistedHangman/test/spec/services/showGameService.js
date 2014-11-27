'use strict';

describe('Service: showGameSevice', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var md1SS = {
    isGameOver: true,
    isGameWon: true,
    workingWordPhrase: '__ __',
    maxPenalties: 6,
    penalties: 2,
    badlyGuessedLetters: ['X', 'Y'],
    guessedLetters: ['A', 'X', 'Y'],
    featureData: {}
  };
  var showGameServiceGame = {
    id: 'id2',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
    puzzleSetter: 'md4',
    gamePhase: 'Playing',
    solverStates: {
      md1: md1SS,
      md2: {isGameOver: true, isGameWon: false},
      md3: {isGameOver: false, isGameWon: false}
    },
    playerScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5},
    gameID: 'id'
  };
  var player = {
    id: 'pid',
    md5: 'md1'
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

  it('computeState without player or game does not work', function () {
    var scope = rootscope.$new();
    cache.put('scope', scope);
    service.computeGameState();
    expect(typeof scope.gameState).toEqual('undefined');
  });

  it('computeState without player does not work', function () {
    var scope = rootscope.$new();
    scope.game = showGameServiceGame;
    cache.put('scope', scope);
    service.computeGameState();
    expect(typeof scope.gameState).toEqual('undefined');
  });

  it('computeState without game does not work', function () {
    var scope = rootscope.$new();
    scope.player = player;
    cache.put('scope', scope);
    service.computeGameState();
    expect(typeof scope.gameState).toEqual('undefined');
  });

  it('computeState for non-thieving game', function () {
    var scope = rootscope.$new();
    scope.player = player;
    scope.game = showGameServiceGame;
    service.initializeScope(scope);
    service.computeGameState();
    expect(scope.gameState).toBe(md1SS);
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman5.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
  });

  it('computeState image for diff max penalties', function () {
    var scope = rootscope.$new();
    scope.player = player;
    scope.game = angular.copy(showGameServiceGame);
    service.initializeScope(scope);
    scope.game.solverStates.md1.maxPenalties = 13;
    service.computeGameState();
    expect(scope.image).toEqual('hangman2.png');
    scope.game.solverStates.md1.maxPenalties = 10;
    service.computeGameState();
    expect(scope.image).toEqual('hangman5.png');
    scope.game.solverStates.md1.maxPenalties = 9;
    service.computeGameState();
    expect(scope.image).toEqual('hangman2.png');
    scope.game.solverStates.md1.maxPenalties = 2;
    service.computeGameState();
    expect(scope.image).toEqual('hangman13.png');
  });

  it('computeState for thieving game', function () {
    var scope = rootscope.$new();
    scope.player = player;
    scope.game = angular.copy(showGameServiceGame);
    scope.game.solverStates.md1.featureData.ThievingPositionTracking = [false, true, false, false, true];
    service.initializeScope(scope);

    service.computeGameState();
    expect(scope.workingWordPhraseClasses).toEqual(['stealablewp', 'stolenwp', 'stealablewp', 'stealablewp', 'stolenwp']);
    expect(scope.image).toEqual('hangman5.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
  });

  it('processGame with new coy', function () {
    var scope = rootscope.$new();
    scope.player = player;
    scope.game = angular.copy(showGameServiceGame);
    service.initializeScope(scope);
    service.computeGameState();

    var update = angular.copy(showGameServiceGame);
    update.lastUpdate = 1345100;
    update.created = 1345000;
    update.solverStates.md1.penalties = 3;
    update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];

    service.processGame(update);
    expect(scope.lastUpdate).toEqual(new Date(1345100000));
    expect(scope.created).toEqual(new Date(1345000000));
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman6.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
  });

  it('processUpdate with no status change', function () {
    var scope = rootscope.$new();
    scope.player = player;
    scope.game = angular.copy(showGameServiceGame);
    service.initializeScope(scope);
    service.computeGameState();

    var update = angular.copy(showGameServiceGame);
    update.lastUpdate = 1345100;
    update.created = 1345000;
    update.solverStates.md1.penalties = 3;
    update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];


    service.processUpdate(update);
    expect(scope.lastUpdate).toEqual(new Date(1345100000));
    expect(scope.created).toEqual(new Date(1345000000));
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman6.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
    expect(rootscope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Playing');
  });

  it('processUpdate with status change', function () {
    var scope = rootscope.$new();
    scope.player = player;
    scope.game = angular.copy(showGameServiceGame);
    scope.game.gamePhase = 'X';
    service.initializeScope(scope);
    service.computeGameState();

    var update = angular.copy(showGameServiceGame);
    update.lastUpdate = 1345100;
    update.created = 1345000;
    update.solverStates.md1.penalties = 3;
    update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];
    update.gamePhase = 'Y';

    service.processUpdate(update);
    expect(scope.lastUpdate).toEqual(new Date(1345100000));
    expect(scope.created).toEqual(new Date(1345000000));
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman6.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
    expect(rootscope.$broadcast).toHaveBeenCalledWith('refreshGames', 'X');
    expect(rootscope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Y');
  });
});

