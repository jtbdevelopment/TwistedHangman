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
  }
};
describe('Controller: RematchCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, http, q, rootScope, deferred, window;

  beforeEach(inject(function ($rootScope, $httpBackend, $q) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    spyOn(rootScope, '$broadcast');
    window = {location: {replace: jasmine.createSpy()}};
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
        if (key == 'scope') {
          return sharedScope
        }
        return null;
      }
    };

    ctrl = $controller('RematchCtrl', {
      $scope: scope,
      twCurrentPlayerService: mockPlayerService,
      $window: window,
      showGameCache: mockShowGameCache
    });

    rootScope.$apply();
  }));

  it('initializes', function () {
    expect(scope.sharedScope).toEqual(sharedScope);
  });
});
