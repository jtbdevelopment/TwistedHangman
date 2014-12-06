'use strict';

describe('Controller: ShowCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var game = {
    id: 'id',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
    wordPhraseSetter: 'md4',
    solverStates: {
      md1: {isPuzzleOver: true, isPuzzleSolved: true, field: 'X'},
      md2: {isPuzzleOver: true, isPuzzleSolved: false, field: 'Y'},
      md3: {isPuzzleOver: false, isPuzzleSolved: false, field: 'Z'}
    },
    playerRunningScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5},
    playerRoundScores: {'md1': 1, 'md2': 0, 'md3': -1, 'md4': 3, 'md5': 2}
  };

  var player = {'player': 'player'};
  var ctrl, scope, http, rootScope, showGameService, q, playerDeferred, location, modal, modalResult;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    spyOn(rootScope, '$broadcast');
    showGameService = jasmine.createSpyObj('showGameService', ['initializeScope', 'processUpdate', 'processGame']);
    location = {path: jasmine.createSpy()};
    modal = {
      open: function (params) {
        console.log('open');
        expect(params.controller).toEqual('ConfirmCtrl');
        expect(params.templateUrl).toEqual('views/confirmDialog.html');
        modalResult = q.defer();
        return {result: modalResult.promise};
      }
    };

    scope = rootScope.$new();

    var mockPlayerService = {
      currentPlayerBaseURL: function () {
        return '/api/player/MANUAL1';
      },
      currentPlayer: function () {
        playerDeferred = q.defer();
        return playerDeferred.promise;
      }
    };

    var mockGameCache = {
      getGameForID: function (id) {
        expect(id).toEqual('gameid');
        return game;
      }
    };
    var routeParams = {
      gameID: 'gameid'
    };


    ctrl = $controller('ShowCtrl', {
      $routeParams: routeParams,
      $rootScope: rootScope,
      $scope: scope,
      $location: location,
      $window: window,
      $modal: modal,
      twCurrentPlayerService: mockPlayerService,
      twShowGameService: showGameService,
      twGameCache: mockGameCache
    });

  }));

  it('initializes', function () {
    expect(showGameService.initializeScope).toHaveBeenCalledWith(scope);

    expect(scope.alerts).toEqual([]);
    expect(scope.player).toBeUndefined();
    playerDeferred.resolve(player);
    rootScope.$apply();
    expect(scope.player).toEqual(player);
    expect(showGameService.processGame).toHaveBeenCalledWith(scope, scope.game);
    expect(showGameService.processGame).toHaveBeenCalledWith(scope, game);
  });

  describe('test various action processing', function () {
    it('post rematch', function () {
      var newGame = {id: 'newid', gamePhase: 'X'};
      http.expectPUT('/api/player/MANUAL1/game/gameid/rematch').respond(newGame);
      scope.startNextRound();
      http.flush();

      expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'X');
      expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'RoundOver');
      expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'NextRoundStarted');
      expect(location.path).toHaveBeenCalledWith('/show/newid');
      expect(showGameService.processGame).toHaveBeenCalledWith(scope, newGame);
    });

    it('post rematch fails', function () {
      http.expectPUT('/api/player/MANUAL1/game/gameid/rematch').respond(501, 'bad stuff');
      scope.startNextRound();
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '501: bad stuff'}]);
    });

    it('accept match', function () {
      var updatedGame = {id: 'newid', gamePhase: 'X'};
      http.expectPUT('/api/player/MANUAL1/game/gameid/accept').respond(updatedGame);
      scope.accept();
      http.flush();

      expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
    });

    it('accept match fails', function () {
      http.expectPUT('/api/player/MANUAL1/game/gameid/accept').respond(501, 'bad stuff');
      scope.accept();
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '501: bad stuff'}]);
    });

    it('reject match', function () {
      var updatedGame = {id: 'newid', gamePhase: 'X'};
      http.expectPUT('/api/player/MANUAL1/game/gameid/reject').respond(updatedGame);
      scope.reject();
      modalResult.resolve();
      http.flush();

      expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
    });

    it('reject match with cancel on confirm', function () {
      scope.reject();
      modalResult.reject();
    });

    it('reject match fails', function () {
      http.expectPUT('/api/player/MANUAL1/game/gameid/reject').respond(502, 'more bad stuff');
      scope.reject();
      modalResult.resolve();
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '502: more bad stuff'}]);
    });

    it('quit match', function () {
      var updatedGame = {id: 'newid', gamePhase: 'X'};
      http.expectPUT('/api/player/MANUAL1/game/gameid/quit').respond(updatedGame);
      scope.quit();
      modalResult.resolve();
      http.flush();

      expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
    });

    it('quit match with cancel on confirm', function () {
      scope.quit();
      modalResult.reject();
    });

    it('quit match fails', function () {
      http.expectPUT('/api/player/MANUAL1/game/gameid/quit').respond(503, 'something');
      scope.quit();
      modalResult.resolve();
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '503: something'}]);
    });

    it('set puzzle', function () {
      var updatedGame = {id: 'newid', gamePhase: 'X'};
      scope.enteredCategory = 'cat';
      scope.enteredWordPhrase = 'wp';
      http.expectPUT('/api/player/MANUAL1/game/gameid/puzzle', {
        category: 'cat',
        wordPhrase: 'wp'
      }).respond(updatedGame);
      scope.setPuzzle();
      http.flush();

      expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
    });

    it('set puzzle fails', function () {
      scope.enteredCategory = 'cat';
      scope.enteredWordPhrase = 'wp';
      http.expectPUT('/api/player/MANUAL1/game/gameid/puzzle', {
        category: 'cat',
        wordPhrase: 'wp'
      }).respond(503, 'something');
      scope.setPuzzle();
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '503: something'}]);
    });

    it('guess letter', function () {
      scope.allowPlayMoves = true;
      var updatedGame = {id: 'newid', gamePhase: 'X'};
      http.expectPUT('/api/player/MANUAL1/game/gameid/guess/a').respond(updatedGame);
      scope.sendGuess('a');
      http.flush();

      expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
    });

    it('guess letter fails', function () {
      scope.allowPlayMoves = true;
      http.expectPUT('/api/player/MANUAL1/game/gameid/guess/a').respond(601, 'bad stuff');
      scope.sendGuess('a');
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '601: bad stuff'}]);
    });

    it('guess letter out of turn', function () {
      scope.allowPlayMoves = false;
      scope.sendGuess('a');

      expect(scope.alerts).toEqual([{type: 'warning', msg: 'Not currently playable.'}]);
    });

    it('steal letter', function () {
      scope.allowPlayMoves = true;
      var updatedGame = {id: 'newid', gamePhase: 'X'};
      http.expectPUT('/api/player/MANUAL1/game/gameid/steal/2').respond(updatedGame);
      scope.stealLetter('2');
      http.flush();

      expect(showGameService.processUpdate).toHaveBeenCalledWith(scope, updatedGame);
    });

    it('steal letter fails', function () {
      scope.allowPlayMoves = true;
      http.expectPUT('/api/player/MANUAL1/game/gameid/steal/2').respond(601, 'bad stuff');
      scope.stealLetter('2');
      http.flush();

      expect(scope.alerts).toEqual([{type: 'danger', msg: '601: bad stuff'}]);
    });

    it('steal letter when not allowed', function () {
      scope.allowPlayMoves = false;
      scope.stealLetter('2');
      expect(scope.alerts).toEqual([{type: 'warning', msg: 'Not currently playable.'}]);
    });
  });

  describe('test various state computation functions', function () {
    beforeEach(function () {
      scope.game = game;
    });

    it('role for player', function () {
      expect(scope.roleForPlayer('md4')).toEqual('Set Puzzle');
      ['md1', 'md2', 'md3', 'md5'].forEach(function (md) {
        expect(scope.roleForPlayer(md)).toEqual('Solver');
      });
    });

    it('role for player with undefined game', function () {
      delete scope.game;
      expect(scope.roleForPlayer('md4')).toEqual('');
    });

    it('gameEndForPlayer for player', function () {
      expect(scope.gameEndForPlayer('md1')).toEqual('Solved!');
      expect(scope.gameEndForPlayer('md2')).toEqual('Hung!');
      expect(scope.gameEndForPlayer('md3')).toEqual('Not Solved.');
      expect(scope.gameEndForPlayer('md4')).toEqual('N/A');
      expect(scope.gameEndForPlayer('md5')).toEqual('Unknown');
    });

    it('gameEndForPlayer with undefined game', function () {
      delete scope.game;
      expect(scope.gameEndForPlayer('md4')).toEqual('');
    });

    it('stateForPlayer for player', function () {
      expect(scope.stateForPlayer('md1', 'field')).toEqual('X');
      expect(scope.stateForPlayer('md2', 'field')).toEqual('Y');
      expect(scope.stateForPlayer('md3', 'field')).toEqual('Z');
      expect(scope.stateForPlayer('md4', 'field')).toEqual('N/A');
      expect(scope.stateForPlayer('md5', 'field')).toEqual('Unknown');
    });

    it('stateForPlayer with undefined game', function () {
      delete scope.game;
      expect(scope.stateForPlayer('md4')).toEqual('');
    });

    it('gameScoreForPlayer with undefined game', function () {
      delete scope.game;
      expect(scope.gameScoreForPlayer('md4')).toEqual('');
    });

    it('gameScoreForPlayer for player', function () {
      angular.forEach({'md1': 1, 'md2': 0, 'md3': -1, 'md4': 3, 'md5': 2}, function (value, key) {
        expect(scope.gameScoreForPlayer(key)).toEqual(value);
      });
    });

    it('runningScoreForPlayer with undefined game', function () {
      delete scope.game;
      expect(scope.runningScoreForPlayer('md4')).toEqual('');
    });

    it('runningScoreForPlayer for player', function () {
      angular.forEach({'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5}, function (value, key) {
        expect(scope.runningScoreForPlayer(key)).toEqual(value);
      });
    });
  });

  describe('test alerts closing', function () {
    it('test closing alerts', function () {
      scope.alerts = ['1', '2'];
      scope.closeAlert(0);
      expect(scope.alerts).toEqual(['2']);
    });

    it('test closing alerts with bad values', function () {
      scope.alerts = ['1', '2'];
      scope.closeAlert(-1);
      expect(scope.alerts).toEqual(['1', '2']);
      scope.closeAlert(2);
      expect(scope.alerts).toEqual(['1', '2']);
      scope.closeAlert();
      expect(scope.alerts).toEqual(['1', '2']);
    });
  });
});
