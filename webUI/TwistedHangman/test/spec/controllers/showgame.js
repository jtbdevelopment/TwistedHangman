'use strict';

describe('Controller: ShowCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var game = {
    id: 'id',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
    puzzleSetter: 'md4',
    solverStates: {
      md1: {isGameOver: true, isGameWon: true},
      md2: {isGameOver: true, isGameWon: false},
      md3: {isGameOver: false, isGameWon: false}
    },
    playerScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5}
  };

  var player = {'player': 'player'};
  var ctrl, scope, http, rootScope, showGameService, q, playerDeferred, location;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    spyOn(rootScope, '$broadcast');
    showGameService = jasmine.createSpyObj('showGameService', ['computeGameState', 'initializeScope', 'processUpdate', 'processGame']);
    location = {path: jasmine.createSpy()};

    scope = rootScope.$new();
    scope.game = game;

    var mockPlayerService = {
      currentPlayerBaseURL: function () {
        return '/api/player/MANUAL1';
      },
      currentPlayer: function () {
        playerDeferred = q.defer();
        return playerDeferred.promise;
      }
    };

    var routeParams = {
      gameID: 'gameid'
    };

    http.expectGET('/api/player/MANUAL1/play/gameid').respond(game);

    ctrl = $controller('ShowCtrl', {
      $routeParams: routeParams,
      $rootScope: rootScope,
      $scope: scope,
      $location: location,
      $window: window,
      twCurrentPlayerService: mockPlayerService,
      twShowGameService: showGameService
    });

  }));

  it('initializes', function () {
    expect(showGameService.initializeScope).toHaveBeenCalledWith(scope);

    expect(scope.player).toBeUndefined();
    playerDeferred.resolve(player);
    rootScope.$apply();
    expect(scope.player).toEqual(player);
    expect(showGameService.computeGameState).toHaveBeenCalledWith(scope);

    http.flush();
    expect(showGameService.processGame).toHaveBeenCalledWith(scope, game);
  });

  it('post rematch', function () {
    var newGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/rematch').respond(newGame);
    scope.startRematch();
    http.flush();

    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'X');
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Rematch');
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Rematched');
    expect(location.path).toHaveBeenCalledWith('/show/newid');
    expect(showGameService.processGame).toHaveBeenCalledWith(scope, newGame);
  });

  it('accept match', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/accept').respond(updatedGame);
    scope.accept();
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
  });

  it('reject match', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/reject').respond(updatedGame);
    scope.reject();
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
  });

  it('set puzzle', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    scope.enteredCategory = 'cat';
    scope.enteredWordPhrase = 'wp';
    http.expectPUT('/api/player/MANUAL1/play/gameid/puzzle', {category: 'cat', wordPhrase: 'wp'}).respond(updatedGame);
    scope.setPuzzle();
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
  });

  it('guess letter', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/guess/a').respond(updatedGame);
    scope.sendGuess('a');
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
  });

  it('steal letter', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/steal/2').respond(updatedGame);
    scope.stealLetter('2');
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
  });

  it('role for player', function () {
    expect(scope.roleForPlayer('md4')).toEqual('Set Puzzle');
    ['md1', 'md2', 'md3', 'md5'].forEach(function (md) {
      expect(scope.roleForPlayer(md)).toEqual('Solver');
    });
  });

  it('gameEndForPlayer for player', function () {
    expect(scope.gameEndForPlayer('md1')).toEqual('Solved');
    expect(scope.gameEndForPlayer('md2')).toEqual('Hung');
    expect(scope.gameEndForPlayer('md3')).toEqual('Incomplete');
    expect(scope.gameEndForPlayer('md4')).toEqual('N/A');
    expect(scope.gameEndForPlayer('md5')).toEqual('Unknown');
  });

  it('gameScoreForPlayer for player', function () {
    ['md1', 'md2', 'md3', 'md4', 'md5'].forEach(function (md) {
      expect(scope.gameScoreForPlayer(md)).toEqual(0);
    });
  });

  it('runningScoreForPlayer for player', function () {

  });

});
