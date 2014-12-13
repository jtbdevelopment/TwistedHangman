'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var MainCtrl, rootScope, scope, playerService, location;
  var player;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    rootScope = $rootScope;
    spyOn(rootScope, '$broadcast').and.callThrough();
    location = {path: jasmine.createSpy()};
    playerService = {
      currentPlayer: function () {
        return player;
      }
    };
    player = {displayName: 'XYZ'};
    MainCtrl = $controller('MainCtrl', {
      $scope: scope,
      $location: location,
      twPlayerService: playerService
    });
  }));

  it('initializes', function () {
    expect(scope.playerGreeting).toEqual('');
    rootScope.$broadcast('playerLoaded');
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome XYZ');
  });

  it('test refresh button', function () {
    scope.refreshGames();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', '');
  });

  it('refreshes on "playerLoaded" broadcast', function () {
    expect(scope.playerGreeting).toEqual('');
    rootScope.$broadcast('playerLoaded');
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome XYZ');

    player = {displayName: 'ABC'};
    rootScope.$broadcast('playerLoaded');
    rootScope.$apply();
    expect(scope.playerGreeting).toEqual('Welcome ABC');
  });
});
