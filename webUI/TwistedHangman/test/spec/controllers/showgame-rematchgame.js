'use strict';

var sharedScope = {
  game: {
    id: 'id',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4'},
    puzzleSetter: 'md4',
    solverStates: {
      'md1': {}
    },
    playerScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3}
  },
  gameID: 'id'
};
describe('Controller: RematchCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, http, rootScope, window, showGameService;

  beforeEach(inject(function ($rootScope, $httpBackend) {
    rootScope = $rootScope;
    http = $httpBackend;
    spyOn(rootScope, '$broadcast');
    window = {location: {replace: jasmine.createSpy()}};
    showGameService = {processGame: jasmine.createSpy()};
  }));

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller) {
    scope = rootScope.$new();
    var mockPlayerService = {
      currentPlayerBaseURL: function () {
        return '/api/player/MANUAL1/';
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

    ctrl = $controller('RematchCtrl', {
      $rootScope: rootScope,
      $scope: scope,
      $window: window,
      twCurrentPlayerService: mockPlayerService,
      twShowGameCache: mockShowGameCache,
      twShowGameService: showGameService
    });
  }));

  it('initializes', function () {
    expect(scope.sharedScope).toEqual(sharedScope);
  });

  it('post rematch', function () {
    var newGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/id/rematch').respond(newGame);
    scope.startRematch();
    http.flush();

    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'X');
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Rematch');
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'Rematched');
    expect(window.location.replace).toHaveBeenCalledWith('#/show/newid');
    expect(showGameService.processGame).toHaveBeenCalledWith(newGame);
  });
});
