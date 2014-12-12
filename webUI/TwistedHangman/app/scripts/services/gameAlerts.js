'use strict';

angular.module('twistedHangmanApp').factory('twGameAlerts',
  ['$rootScope', 'twGameDetails', 'twCurrentPlayerService',
    function ($rootScope, twGameDetails, twCurrentPlayerService) {
      var currentMD5;

      function loadMD5() {
        twCurrentPlayerService.currentPlayer().then(function (player) {
          currentMD5 = player.md5;
        });
      }

      loadMD5();

      $rootScope.$on('playerSwitch', function () {
        loadMD5();
      });

      return {
        md5: function () {
          return currentMD5;
        },
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
