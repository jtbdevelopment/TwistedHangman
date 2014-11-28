'use strict';

describe('Controller: ShowCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var game = {'mygame': 'game'};
  var player = {'player': 'player'};
  var ctrl, scope, http, rootScope, showGameService, q, playerDeferred, location;

  beforeEach(inject(function ($rootScope, $httpBackend, $q) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    spyOn(rootScope, '$broadcast');
    showGameService = {
      computeGameState: jasmine.createSpy(),
      initializeScope: jasmine.createSpy(),
      processUpdate: jasmine.createSpy(),
      processGame: jasmine.createSpy()
    };
    location = {path: jasmine.createSpy()};
  }));

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller) {
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

    expect(typeof scope.player).toEqual('undefined');
    playerDeferred.resolve(player);
    rootScope.$apply();
    expect(scope.player).toEqual(player);
    expect(showGameService.computeGameState).toHaveBeenCalled();

    http.flush();
    expect(showGameService.processGame).toHaveBeenCalledWith(game);
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
    expect(showGameService.processGame).toHaveBeenCalledWith(newGame);
  });

  it('guess letter', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/guess/a').respond(updatedGame);
    scope.sendGuess('a');
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(updatedGame);
  });

  it('steal letter', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/gameid/steal/2').respond(updatedGame);
    scope.stealLetter('2');
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(updatedGame);
  });
});
