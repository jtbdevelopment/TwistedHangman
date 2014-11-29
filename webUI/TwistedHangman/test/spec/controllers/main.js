'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var MainCtrl, rootScope, scope, playerService, q, deferredPlayer;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $q) {
    scope = $rootScope.$new();
    rootScope = $rootScope;
    q = $q;
    spyOn(rootScope, '$broadcast');
    playerService = {
      currentPlayer: function () {
        deferredPlayer = $q.defer();
        return deferredPlayer.promise;
      }
    };
    MainCtrl = $controller('MainCtrl', {
      $scope: scope,
      twCurrentPlayerService: playerService
    });
  }));

  it('initializes', function () {
    deferredPlayer.resolve({displayName: 'XYZ'});
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome XYZ');
  });

  it('test refresh button', function () {
    scope.refreshGames();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', '');
  });

  it('refreshes on "playerSwitch" broadcast', function () {
    rootScope.$broadcast('playerSwitch');
    deferredPlayer.resolve({displayName: 'ABC'});
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome ABC');
  });
});
