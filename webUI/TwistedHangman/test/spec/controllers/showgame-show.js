'use strict';

describe('Controller: ShowCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var game = {'mygame': 'game'};
  var player = {'player': 'player'};
  var ctrl, scope, http, rootScope, showGameService, q, deferred;

  beforeEach(inject(function ($rootScope, $httpBackend, $q) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    showGameService = {
      computeGameState: jasmine.createSpy(),
      initializeScope: jasmine.createSpy(),
      processGame: jasmine.createSpy()
    };
  }));

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller) {
    scope = rootScope.$new();
    var mockPlayerService = {
      currentPlayerBaseURL: function () {
        return '/api/player/MANUAL1/';
      },
      currentPlayer: function () {
        deferred = q.defer();
        return deferred.promise;
      }
    };

    var mockShowGameCache = {
      get: function (key) {
        if (key === 'scope') {
          return sharedScope;
        }
        return null;
      }
    };

    var routeParams = {
      gameID: 'gameid'
    }

    http.expectGET('/api/player/MANUAL1/play/gameid').respond(game);

    ctrl = $controller('ShowCtrl', {
      $routeParams: routeParams,
      $rootScope: rootScope,
      $scope: scope,
      $window: window,
      twCurrentPlayerService: mockPlayerService,
      twShowGameCache: mockShowGameCache,
      twShowGameService: showGameService
    });

  }));

  it('initializes', function () {
    expect(showGameService.initializeScope).toHaveBeenCalledWith(scope);

    expect(typeof scope.player).toEqual('undefined');
    deferred.resolve(player);
    rootScope.$apply();
    expect(scope.player).toEqual(player);
    expect(showGameService.computeGameState).toHaveBeenCalled();

    http.flush();
    expect(showGameService.processGame).toHaveBeenCalledWith(game);
  });

});
