'use strict';

describe('Service: gameAlerts', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var rootScope, service;
  var md5value = 'md5', playerDeferred, twGameDetails;
  var player = {md5: md5value};
  var gameDetails = {};
  beforeEach(module(function ($provide) {
    $provide.factory('twCurrentPlayerService', ['$q', function ($q) {
      return {
        currentPlayer: function () {
          playerDeferred = $q.defer();
          return playerDeferred.promise;
        }
      };
    }]);
    $provide.factory('twGameDetails', function () {
      return gameDetails;
    });
  }));

  beforeEach(inject(function ($rootScope, $injector) {
    rootScope = $rootScope;
    spyOn(rootScope, '$broadcast').and.callThrough();
    service = $injector.get('twGameAlerts');
    playerDeferred.resolve(player);
    rootScope.$apply();
  }));

  it('initializes', function () {
    expect(service.md5()).toEqual(md5value);
  });

  it('listens for playerSwitch', function () {
    rootScope.$broadcast('playerSwitch');
    playerDeferred.resolve({md5: 'newmd5'});
    rootScope.$apply();
    expect(service.md5()).toEqual('newmd5');
  });

  describe('new game entry alerts', function () {
    it('publishes new game alert', function () {
      var newgame = {id: 'x', stuff: 'here'};
      service.checkNewEntryForAlerts(newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('newGameEntry', 'x', newgame);
    });
  });

  describe('game update alerts', function () {
    var oldgame, newgame;
    beforeEach(function () {
      oldgame = {
        id: 'x',
        gamePhase: 'Setup'
      };

      newgame = angular.copy(oldgame);
    });

    it('publishes alert on phase change', function () {
      newgame.gamePhase = 'SetupX';
      service.checkUpdateForAlerts(oldgame, newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChange', newgame.id, newgame);
    });

    it('does not publish alert on non-phase change', function () {
      service.checkUpdateForAlerts(oldgame, newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalled();
    });
  });
});
