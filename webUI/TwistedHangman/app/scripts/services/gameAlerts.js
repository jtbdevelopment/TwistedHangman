'use strict';

angular.module('twistedHangmanApp').factory('twGameAlerts',
  ['$rootScope',
    function ($rootScope) {
      return {
        checkUpdateForAlerts: function (oldgame, newgame) {
          if (oldgame.gamePhase !== newgame.gamePhase) {
            $rootScope.$broadcast('phaseChange', newgame.id, newgame);
          }
        },
        checkNewEntryForAlerts: function (newgame) {
          $rootScope.$broadcast('newGameEntry', newgame.id, newgame);
        }
      };
    }
  ]);
