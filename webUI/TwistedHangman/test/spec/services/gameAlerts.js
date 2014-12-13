'use strict';

describe('Service: gameAlerts', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var rootScope, service;
  var md5value = 'md5', playerDeferred;
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
    afterEach(function () {
      delete gameDetails.playerChallengeResponseNeeded;
      delete gameDetails.playerCanPlay;
      delete gameDetails.playerSetupEntryRequired;
      delete gameDetails.gameEndForPlayer;
    });

    it('publishes new game alert - set 1', function () {
      var newgame = {id: 'x', stuff: 'here', gamePhase: 'Quit'};
      gameDetails.playerChallengeResponseNeeded = function () {
        return false;
      };
      gameDetails.playerCanPlay = function () {
        return true;
      };
      gameDetails.playerSetupEntryRequired = function () {
        return false;
      };
      gameDetails.gameEndForPlayer = function () {
        return 'Hung!';
      };
      service.checkNewEntryForAlerts(newgame);

      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('roundOverAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('quitAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('hungAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('solvedAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('setupAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('playAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('challengedAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
    });

    it('publishes new game alert - set 2', function () {
      var newgame = {id: 'x', stuff: 'here'};
      gameDetails.playerChallengeResponseNeeded = function () {
        return true;
      };
      gameDetails.playerCanPlay = function () {
        return false;
      };
      gameDetails.playerSetupEntryRequired = function () {
        return true;
      };
      gameDetails.gameEndForPlayer = function () {
        return 'Solved!';
      };
      service.checkNewEntryForAlerts(newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('roundOverAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('quitAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('hungAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('solvedAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('setupAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('playAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('challengedAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
    });

    it('publishes new game alert - set 3', function () {
      var newgame = {id: 'x', stuff: 'here', gamePhase: 'RoundOver'};
      gameDetails.playerChallengeResponseNeeded = function () {
        return true;
      };
      gameDetails.playerCanPlay = function () {
        return false;
      };
      gameDetails.playerSetupEntryRequired = function () {
        return true;
      };
      gameDetails.gameEndForPlayer = function () {
        return 'X';
      };
      service.checkNewEntryForAlerts(newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('roundOverAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('quitAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('hungAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('solvedAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('setupAlert', newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('playAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('challengedAlert', newgame);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
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
      expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
    });

    it('does not publish alert on non-phase change', function () {
      service.checkUpdateForAlerts(oldgame, newgame);
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('phaseChangeAlert', newgame);
    });
  });
});
