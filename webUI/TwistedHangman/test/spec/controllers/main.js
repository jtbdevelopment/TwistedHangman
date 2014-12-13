'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var MainCtrl, rootScope, scope, playerService, q, deferredPlayer, location;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $q) {
    scope = $rootScope.$new();
    rootScope = $rootScope;
    q = $q;
    spyOn(rootScope, '$broadcast').and.callThrough();
    location = {path: jasmine.createSpy()};
    playerService = {
      currentPlayer: function () {
        deferredPlayer = $q.defer();
        return deferredPlayer.promise;
      }
    };
    MainCtrl = $controller('MainCtrl', {
      $scope: scope,
      $location: location,
      twPlayerService: playerService
    });
  }));

  it('initializes', function () {
    deferredPlayer.resolve({displayName: 'XYZ'});
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome XYZ');
  });

  it('initializes to error', function () {
    deferredPlayer.reject();
    rootScope.$apply();
    expect(location.path).toHaveBeenCalledWith('/error');
  });

  it('test refresh button', function () {
    scope.refreshGames();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', '');
  });

  it('refreshes on "playerSwitch" broadcast', function () {
    deferredPlayer.resolve({displayName: 'XYZ'});
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome XYZ');
    console.warn('running test');
    rootScope.$broadcast('playerSwitch', '');
    deferredPlayer.resolve({displayName: 'ABC'});
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome ABC');
  });
});
