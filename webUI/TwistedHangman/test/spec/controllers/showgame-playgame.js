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
describe('Controller: PlayCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, http, showGameService;

  beforeEach(inject(function ($httpBackend) {
    http = $httpBackend;
    showGameService = {processUpdate: jasmine.createSpy()};
  }));

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $controller) {
    scope = $rootScope.$new();
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

    ctrl = $controller('PlayCtrl', {
      $scope: scope,
      twCurrentPlayerService: mockPlayerService,
      twShowGameCache: mockShowGameCache,
      twShowGameService: showGameService
    });
  }));

  it('initializes', function () {
    expect(scope.sharedScope).toEqual(sharedScope);
  });

  it('guess letter', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/id/guess/a').respond(updatedGame);
    scope.sendGuess('a');
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(updatedGame);
  });

  it('steal letter', function () {
    var updatedGame = {id: 'newid', gamePhase: 'X'};
    http.expectPUT('/api/player/MANUAL1/play/id/steal/2').respond(updatedGame);
    scope.stealLetter('2');
    http.flush();

    expect(showGameService.processUpdate).toHaveBeenCalledWith(updatedGame);
  });
});
