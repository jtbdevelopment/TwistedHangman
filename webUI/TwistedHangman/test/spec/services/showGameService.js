'use strict';

describe('Service: showGameSevice', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var md1SS = {
    isPuzzleOver: true,
    isPuzzleSolved: true,
    workingWordPhrase: '__ A_',
    maxPenalties: 6,
    penalties: 2,
    badlyGuessedLetters: ['X', 'Y'],
    guessedLetters: ['A', 'X', 'Y'],
    featureData: {}
  };
  var showGameServiceGame = {
    id: 'id2',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
    wordPhraseSetter: 'md4',
    gamePhase: 'Playing',
    solverStates: {
      md1: md1SS,
      md2: {isPuzzleOver: true, isPuzzleSolved: false},
      md3: {isPuzzleOver: false, isPuzzleSolved: false}
    },
    playerRunningScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5},
    gameID: 'id',
    featureData: {},
    features: []
  };
  var player = {
    id: 'pid',
    md5: 'md1'
  };

  var service, rootscope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $injector) {
    rootscope = $rootScope;
    spyOn($rootScope, '$broadcast');
    service = $injector.get('twShowGameService');
  }));

  it('initialize scope', function () {
    var scope = rootscope.$new();

    scope.game = showGameServiceGame;
    service.initializeScope(scope);
    expect(scope.workingWordPhraseArray).toEqual([]);
    expect(scope.workingWordPhraseClasses).toEqual([]);
    expect(scope.letters).toEqual(['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']);
    expect(scope.letterClasses).toEqual(['regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular']);
  });

  it('computeState without player or game does not work', function () {
    var scope = rootscope.$new();
    service.processGame(scope);
    expect(scope.gameState).toBeUndefined();
  });

  it('computeState without player does not work', function () {
    var scope = rootscope.$new();
    service.processGame(scope, showGameServiceGame);
    expect(scope.gameState).toBeUndefined();
  });

  it('computeState without game does not work', function () {
    var scope = rootscope.$new();
    scope.player = player;
    service.processGame(scope);
    expect(scope.gameState).toBeUndefined();
  });

  it('computeState for non-thieving game', function () {
    var scope = rootscope.$new();
    scope.player = player;
    service.initializeScope(scope);
    service.processGame(scope, showGameServiceGame);
    expect(scope.gameState).toBe(md1SS);
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman5.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
  });

  it('computeState image for diff max penalties', function () {
    var scope = rootscope.$new();
    scope.player = player;
    var game = angular.copy(showGameServiceGame);
    service.initializeScope(scope);
    game.solverStates.md1.maxPenalties = 13;
    service.processGame(scope, game);
    expect(scope.image).toEqual('hangman2.png');
    game.solverStates.md1.maxPenalties = 10;
    service.processGame(scope, game);
    expect(scope.image).toEqual('hangman5.png');
    game.solverStates.md1.maxPenalties = 9;
    service.processGame(scope, game);
    expect(scope.image).toEqual('hangman2.png');
    game.solverStates.md1.maxPenalties = 2;
    service.processGame(scope, game);
    expect(scope.image).toEqual('hangman13.png');
  });

  it('computeState for thieving game', function () {
    var scope = rootscope.$new();
    scope.player = player;
    var game = angular.copy(showGameServiceGame);
    game.solverStates.md1.featureData.ThievingPositionTracking = [false, true, false, false, true];
    game.solverStates.md1.featureData.ThievingLetters = ['B'];
    service.initializeScope(scope);
    service.processGame(scope, game);
    expect(scope.workingWordPhraseClasses).toEqual(['stealablewp', 'stolenwp', 'regularwp', 'regularwp', 'stolenwp']);
    expect(scope.image).toEqual('hangman5.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'stolenkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
  });

  it('processGame with new copy', function () {
    var scope = rootscope.$new();
    scope.player = player;
    var game = angular.copy(showGameServiceGame);
    service.initializeScope(scope);
    service.processGame(scope, game);

    var update = angular.copy(showGameServiceGame);
    update.lastUpdate = 1345100;
    update.created = 1345000;
    update.solverStates.md1.penalties = 3;
    update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];

    service.processGame(scope, update);
    expect(scope.lastUpdate).toEqual(new Date(1345100));
    expect(scope.created).toEqual(new Date(1345000));
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman6.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
  });

  it('processUpdate with no status change', function () {
    var scope = rootscope.$new();
    scope.player = player;
    var game = angular.copy(showGameServiceGame);
    service.initializeScope(scope);
    service.processGame(scope, game);

    var update = angular.copy(showGameServiceGame);
    update.lastUpdate = 1345100;
    update.created = 1345000;
    update.solverStates.md1.penalties = 3;
    update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];


    service.processUpdate(scope, update);
    expect(scope.lastUpdate).toEqual(new Date(1345100));
    expect(scope.created).toEqual(new Date(1345000));
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman6.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
    expect(rootscope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Playing');
  });

  it('processUpdate with status change', function () {
    var scope = rootscope.$new();
    scope.player = player;
    var game = angular.copy(showGameServiceGame);
    game.gamePhase = 'X';
    service.initializeScope(scope);
    service.processGame(scope, game);

    var update = angular.copy(showGameServiceGame);
    update.lastUpdate = 1345100;
    update.created = 1345000;
    update.solverStates.md1.penalties = 3;
    update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];
    update.gamePhase = 'Y';

    service.processUpdate(scope, update);
    expect(scope.lastUpdate).toEqual(new Date(1345100));
    expect(scope.created).toEqual(new Date(1345000));
    expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
    expect(scope.image).toEqual('hangman6.png');
    expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
    expect(rootscope.$broadcast).toHaveBeenCalledWith('refreshGames', 'X');
    expect(rootscope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Y');
  });
});

