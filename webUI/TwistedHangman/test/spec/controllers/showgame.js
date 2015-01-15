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
  var ctrl, scope, http, rootScope, gameDisplay, q, location, modal, modalResult, controller;
  var gameCacheExpectedId, gameCacheReturnResult, mockPlayerService, mockGameCache, routeParams, mockGameDetails;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
    rootScope = $rootScope;
    http = $httpBackend;
    controller = $controller;
    q = $q;
    spyOn(rootScope, '$broadcast').and.callThrough();
    gameDisplay = jasmine.createSpyObj('gameDisplay', ['initializeScope', 'processGameUpdateForScope', 'updateScopeForGame']);
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
        return player;
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
        twPlayerService: mockPlayerService,
        twGameDisplay: gameDisplay,
        twGameCache: mockGameCache,
        twGameDetails: mockGameDetails
      });
    });

    it('initializes', function () {
      expect(gameDisplay.initializeScope).toHaveBeenCalledWith(scope);

      expect(scope.player).toEqual(player);
      expect(gameDisplay.updateScopeForGame).toHaveBeenCalledWith(scope, game);
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
        twPlayerService: mockPlayerService,
        twGameDisplay: gameDisplay,
        twGameCache: mockGameCache,
        twGameDetails: mockGameDetails
      });
    });

    it('initializes with no game', function () {
      expect(scope.gameDetails).toBe(mockGameDetails);
      expect(gameDisplay.initializeScope).toHaveBeenCalledWith(scope);
      expect(scope.player).toEqual(player);
      expect(scope.game).toBeUndefined();
      expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith(scope, game);
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
        twPlayerService: mockPlayerService,
        twGameDisplay: gameDisplay,
        twGameCache: mockGameCache,
        twGameDetails: mockGameDetails
      });
    });

    describe('checking for posting errors using game error modal', function () {
      var modalMessage, confirmModalOpen;
      beforeEach(function () {
        confirmModalOpen = modal.open;
        modal.open = function (params) {
          expect(params.controller).toEqual('ErrorCtrl');
          expect(params.templateUrl).toEqual('views/gameErrorDialog.html');
          expect(params.resolve.message()).toEqual(modalMessage);
          modalResult = q.defer();
          return {result: modalResult.promise};
        };
      });

      it('reject match fails', function () {
        //  Two modals in this one
        http.expectPUT('/api/player/MANUAL1/game/gameid/reject').respond(502, 'more bad stuff');
        var errorOpen = modal.open;
        modal.open = confirmModalOpen;
        scope.reject();
        modalResult.resolve();
        modal.open = errorOpen;
        modalMessage = '502: more bad stuff';
        modalResult.resolve();
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
        modalMessage = 'Not currently playable.';
        scope.stealLetter('2');
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
        modal.open = confirmModalOpen;
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

    describe('listens for playerLoaded broadcasts', function () {
      it('test playerLoaded', function () {
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(location.path).toHaveBeenCalledWith('/');
      });
    });

    describe('listens for gameUpdate broadcasts', function () {
      it('listens for gameUpdate and updates if same game id', function () {
        scope.game = game;
        var gameUpdate = angular.copy(game);
        rootScope.$broadcast('gameUpdate', game.id, game);
        rootScope.$apply();
        expect(gameDisplay.updateScopeForGame).toHaveBeenCalledWith(scope, gameUpdate);
      });

      it('listens for gameUpdate and ignores if different game id', function () {
        scope.game = game;
        var gameUpdate = angular.copy(game);
        gameUpdate.id = 'notid';
        rootScope.$broadcast('gameUpdate', game.id, game);
        rootScope.$apply();
        expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith(scope, gameUpdate);
      });

      it('listens for gameUpdate and ignores if scope has no game', function () {
        var gameUpdate = angular.copy(game);
        gameUpdate.id = 'notid';
        rootScope.$broadcast('gameUpdate', game.id, game);
        rootScope.$apply();
        expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith(scope, gameUpdate);
      });
    });

    describe('listens for gameCachesLoaded broadcasts', function () {
      it('listens for gameCachesLoaded and updates from game', function () {
        scope.game = game;
        var gameUpdate = angular.copy(game);
        gameCacheReturnResult = gameUpdate;
        rootScope.$broadcast('gameCachesLoaded');
        rootScope.$apply();
        expect(gameDisplay.updateScopeForGame).toHaveBeenCalledWith(scope, gameUpdate);
      });

      it('listens for gameCachesLoaded and goes to main page if no longer valid', function () {
        scope.game = game;
        var gameUpdate;
        gameCacheReturnResult = gameUpdate;
        rootScope.$broadcast('gameCachesLoaded');
        rootScope.$apply();
        expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith(scope, gameUpdate);
        expect(location.path).toHaveBeenCalledWith('/');
      });
    });

    describe('test various action processing', function () {
      it('post rematch', function () {
        var newGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/rematch').respond(newGame);
        scope.startNextRound();
        http.flush();

        expect(location.path).toHaveBeenCalledWith('/show/newid');
        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, newGame);
      });

      it('accept match', function () {
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/accept').respond(updatedGame);
        scope.accept();
        http.flush();

        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('reject match', function () {
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/reject').respond(updatedGame);
        scope.reject();
        modalResult.resolve();
        http.flush();

        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
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

        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
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

        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('guess letter', function () {
        scope.allowPlayMoves = true;
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/guess/a').respond(updatedGame);
        scope.sendGuess('a');
        http.flush();

        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });

      it('steal letter', function () {
        scope.allowPlayMoves = true;
        var updatedGame = {id: 'newid', gamePhase: 'X'};
        http.expectPUT('/api/player/MANUAL1/game/gameid/steal/2').respond(updatedGame);
        scope.stealLetter('2');
        http.flush();

        expect(gameDisplay.processGameUpdateForScope).toHaveBeenCalledWith(scope, updatedGame);
      });
    });
  });
});
