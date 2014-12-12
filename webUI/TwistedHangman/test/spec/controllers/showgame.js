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
  var ctrl, scope, http, rootScope, showGameService, q, playerDeferred, location, modal, modalResult, controller;
  var gameCacheExpectedId, gameCacheReturnResult, mockPlayerService, mockGameCache, routeParams, mockGameDetails;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
    rootScope = $rootScope;
    http = $httpBackend;
    controller = $controller;
    q = $q;
    spyOn(rootScope, '$broadcast').and.callThrough();
    showGameService = jasmine.createSpyObj('showGameService', ['initializeScope', 'processGameUpdateForScope', 'updateScopeForGame']);
    location = {path: jasmine.createSpy()};
    modal = {
      open: function (params) {
        expect(params.controller).toEqual('ConfirmCtrl');
        expect(params.templateUrl).toEqual('views/confirmDialog.html');
        modalResult = q.defer();
        return {result: modalResult.promise};
      }
    };

    scope = rootScope.$new();

    mockPlayerService = {
      currentPlayerBaseURL: function () {
        return '/api/player/MANUAL1';
      },
      currentPlayer: function () {
        playerDeferred = q.defer();
        return playerDeferred.promise;
      }
    };

    mockGameCache = {
      getGameForID: function (id) {
        expect(id).toEqual(gameCacheExpectedId);
        return gameCacheReturnResult;
      }
    };

    mockGameDetails = {};

    routeParams = {
      gameID: 'gameid'
    };


  }));

  describe('good initialization', function () {
    beforeEach(function () {
      gameCacheExpectedId = 'gameid';
      gameCacheReturnResult = game;

      ctrl = controller('ShowCtrl', {
        $routeParams: routeParams,
        $rootScope: rootScope,
        $scope: scope,
        $location: location,
        $window: window,
        $modal: modal,
        twCurrentPlayerService: mockPlayerService,
        twShowGameService: showGameService,
        twGameCache: mockGameCache,
        twGameDetails: mockGameDetails
      });
    });

    it('initializes', function () {
      expect(showGameService.initializeScope).toHaveBeenCalledWith(scope);

      expect(showGameService.updateScopeForGame).toHaveBeenCalledWith(scope, game);
      expect(scope.player).toBeUndefined();
      playerDeferred.resolve(player);
      rootScope.$apply();
      expect(scope.player).toEqual(player);
      expect(showGameService.updateScopeForGame).toHaveBeenCalledWith(scope, scope.game);
      expect(scope.gameDetails).toBe(mockGameDetails);
    });
  });

  describe('bad initialization', function () {
    beforeEach(function () {
      gameCacheExpectedId = 'gameid';
      gameCacheReturnResult = undefined;

      ctrl = controller('ShowCtrl', {
        $routeParams: routeParams,
        $rootScope: rootScope,
        $scope: scope,
        $location: location,
        $window: window,
        $modal: modal,
        twCurrentPlayerService: mockPlayerService,
        twShowGameService: showGameService,
        twGameCache: mockGameCache,
        twGameDetails: mockGameDetails
      });
    });

    it('initializes with no game', function () {
      expect(scope.gameDetails).toBe(mockGameDetails);
      expect(showGameService.initializeScope).toHaveBeenCalledWith(scope);
      expect(showGameService.updateScopeForGame).not.toHaveBeenCalledWith(scope, game);

      expect(scope.player).toBeUndefined();
      playerDeferred.resolve(player);
      rootScope.$apply();
      expect(scope.player).toEqual(player);
      expect(scope.game).toBeUndefined();
      expect(showGameService.updateScopeForGame).toHaveBeenCalledWith(scope, scope.game);
    });

    it('initializes with no player', function () {
      expect(scope.gameDetails).toBe(mockGameDetails);
      expect(showGameService.initializeScope).toHaveBeenCalledWith(scope);
      expect(showGameService.updateScopeForGame).not.toHaveBeenCalledWith(scope, game);

      expect(scope.player).toBeUndefined();
      playerDeferred.reject();
      rootScope.$apply();
      expect(scope.player).toBeUndefined();
      expect(scope.game).toBeUndefined();
      expect(location.path).toHaveBeenCalledWith('/error');
    });
  });

  describe('post initialization tests', function () {
    beforeEach(function () {
      gameCacheExpectedId = 'gameid';
      gameCacheReturnResult = game;

      ctrl = controller('ShowCtrl', {
        $routeParams: routeParams,
        $rootScope: rootScope,
        $scope: scope,
        $location: location,
        $window: window,
        $modal: modal,
        twCurrentPlayerService: mockPlayerService,
        twShowGameService: showGameService,
        twGameCache: mockGameCache,
        twGameDetails: mockGameDetails
      });
    });

    describe('checking for posting errors using game error modal', function () {
      var modalMessage, confirmModelOpen;
      beforeEach(function () {
        confirmModelOpen = modal.open;
        modal.open = function (params) {
          console.log('here');
          expect(params.controller).toEqual('ErrorCtrl');
          expect(params.templateUrl).toEqual('views/gameErrorDialog.html');
          expect(params.resolve.message()).toEqual(modalMessage);
          modalResult = q.defer();
          return {result: modalResult.promise};
        };

        it('reject match fails', function () {
          http.expectPUT('/api/player/MANUAL1/game/gameid/reject').respond(502, 'more bad stuff');
          scope.reject();
          modalResult.resolve();
          modalMessage = '502: more bad stuff';
          http.flush();

        });

        it('steal letter fails', function () {
          scope.allowPlayMoves = true;
          http.expectPUT('/api/player/MANUAL1/game/gameid/steal/2').respond(601, 'bad stuff');
          scope.stealLetter('2');
          modalMessage = '601: bad stuff';
          http.flush();
        });

        it('steal letter when not allowed', function () {
          scope.allowPlayMoves = false;
          scope.stealLetter('2');
          modalMessage = 'Not currently playable.';
        });
      });

      it('post rematch fails', function () {
        http.expectPUT('/api/player/MANUAL1/game/gameid/rematch').respond(501, 'bad stuff');
        scope.startNextRound();
        modalMessage = '501: bad stuff';
        http.flush();
      });

      it('accept match fails', function () {
        http.expectPUT('/api/player/MANUAL1/game/gameid/accept').respond(501, 'bad stuff');
        scope.accept();
        modalMessage = '501: bad stuff';
        http.flush();
      });

      it('set puzzle fails', function () {
        scope.enteredCategory = 'cat';
        scope.enteredWordPhrase = 'wp';
        http.expectPUT('/api/player/MANUAL1/game/gameid/puzzle', {
          category: 'cat',
          wordPhrase: 'wp'
        }).respond(503, 'something');
        scope.setPuzzle();
        modalMessage = '503: something';
        http.flush();
      });

      it('quit match fails', function () {

        //  Two modals in this one

        http.expectPUT('/api/player/MANUAL1/game/gameid/quit').respond(503, 'something');
        var errorOpen = modal.open;
        modal.open = confirmModelOpen;
        scope.quit();
        modalResult.resolve();
        modal.open = errorOpen;
        modalMessage = '503: something';
        http.flush();
      });

      it('guess letter fails', function () {
        scope.allowPlayMoves = true;
        http.expectPUT('/api/player/MANUAL1/game/gameid/guess/a').respond(601, 'bad stuff');
        scope.sendGuess('a');
        modalMessage = '601: bad stuff';
        http.flush();
      });

      it('guess letter out of turn', function () {
        scope.allowPlayMoves = false;
        modalMessage = 'Not currently playable.';
        scope.sendGuess('a');
      });

    });
    describe('listens for gameUpdate broadcasts', function () {
      it('listens for gameUpdate and updates if same game id', function () {
        scope.game = game;
        var gameUpdate = angular.copy(game);
        rootScope.$broadcast('gameUpdate', game.id, game);
        rootScope.$apply();
        expect(showGameService.updateScopeForGame).toHaveBeenCalledWith(scope, gameUpdate);
      });

      it('listens for gameUpdate and ignores if different game id', function () {
        scope.game = game;
        var gameUpdate = angular.copy(game);
        gameUpdate.id = 'notid';
        rootScope.$broadcast('gameUpdate', game.id, game);
        rootScope.$apply();
        expect(showGameService.updateScopeForGame).not.toHaveBeenCalledWith(scope, gameUpdate);
      });

      it('listens for gameUpdate and ignores if scope has no game', function () {
        var gameUpdate = angular.copy(game);
        gameUpdate.id = 'notid';
        rootScope.$broadcast('gameUpdate', game.id, game);
        rootScope.$apply();
        expect(showGameService.updateScopeForGame).not.toHaveBeenCalledWith(scope, gameUpdate);
      });
    });

    describe('listens for gameCachesLoaded broadcasts', function () {
      it('listens for gameCachesLoaded and updates from game', function () {
        scope.game = game;
        var gameUpdate = angular.copy(game);
        gameCacheReturnResult = gameUpdate;
        rootScope.$broadcast('gameCachesLoaded');
        rootScope.$apply();
        expect(showGameService.updateScopeForGame).toHaveBeenCalledWith(scope, gameUpdate);
      });

      it('listens for gameCachesLoaded and goes to main page if no longer valid', function () {
        scope.game = game;
        var gameUpdate;
        gameCacheReturnResult = gameUpdate;
        rootScope.$broadcast('gameCachesLoaded');
        rootScope.$apply();
        expect(showGameService.updateScopeForGame).not.toHaveBeenCalledWith(scope, gameUpdate);
        expect(location.path).toHaveBeenCalledWith('/');
      });
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
        expect(showGameService.updateScopeForGame).toHaveBeenCalledWith(scope, newGame);
      });

      it('accept match', function () {
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/accept').respond(updatedGame);
        scope.accept();
        http.flush();

        expect(showGameService.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('reject match', function () {
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/reject').respond(updatedGame);
        scope.reject();
        modalResult.resolve();
        http.flush();

        expect(showGameService.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('reject match with cancel on confirm', function () {
        scope.reject();
        modalResult.reject();
      });

      it('quit match', function () {
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/quit').respond(updatedGame);
        scope.quit();
        modalResult.resolve();
        http.flush();

        expect(showGameService.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('quit match with cancel on confirm', function () {
        scope.quit();
        modalResult.reject();
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

        expect(showGameService.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('guess letter', function () {
        scope.allowPlayMoves = true;
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/guess/a').respond(updatedGame);
        scope.sendGuess('a');
        http.flush();

        expect(showGameService.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('steal letter', function () {
        scope.allowPlayMoves = true;
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/steal/2').respond(updatedGame);
        scope.stealLetter('2');
        http.flush();

        expect(showGameService.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });
    });
  });
});
