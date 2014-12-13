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

          if (newgame.gamePhase === 'RoundOver' && oldgame.gamePhase !== 'RoundOver') {
            $rootScope.$broadcast('roundOverAlert', newgame);
          }
          if (newgame.gamePhase === 'Quit' && oldgame.gamePhase !== 'Quit') {
            $rootScope.$broadcast('quitAlert', newgame);
          }

          var newGameEnd = twGameDetails.gameEndForPlayer(newgame, currentMD5);
          var oldGameEnd = twGameDetails.gameEndForPlayer(oldgame, currentMD5);
          if (newGameEnd !== oldGameEnd) {
            switch (newGameEnd) {
              case 'Solved!':
                $rootScope.$broadcast('solvedAlert', newgame);
                break;
              case 'Hung!':
                $rootScope.$broadcast('hungAlert', newgame);
                break;
            }
          }
          if (twGameDetails.playerSetupEntryRequired(newgame, currentMD5) !== twGameDetails.playerSetupEntryRequired(oldgame, currentMD5)) {
            $rootScope.$broadcast('setupAlert', newgame);
          }
          if (twGameDetails.playerCanPlay(newgame, currentMD5) !== twGameDetails.playerCanPlay(oldgame, currentMD5)) {
            $rootScope.$broadcast('playAlert', newgame);
          }
          if (twGameDetails.playerChallengeResponseNeeded(newgame, currentMD5) !== twGameDetails.playerChallengeResponseNeeded(oldgame, currentMD5)) {
            $rootScope.$broadcast('challengedAlert', newgame);
          }
          if (oldgame.gamePhase !== newgame.gamePhase) {
            $rootScope.$broadcast('phaseChangeAlert', newgame);
          }
        },
        checkNewEntryForAlerts: function (newgame) {
          if (newgame.gamePhase === 'RoundOver') {
            $rootScope.$broadcast('roundOverAlert', newgame);
          }
          if (newgame.gamePhase === 'Quit') {
            $rootScope.$broadcast('quitAlert', newgame);
          }
          switch (twGameDetails.gameEndForPlayer(newgame, currentMD5)) {
            case 'Solved!':
              $rootScope.$broadcast('solvedAlert', newgame);
              break;
            case 'Hung!':
              $rootScope.$broadcast('hungAlert', newgame);
              break;
          }
          if (twGameDetails.playerSetupEntryRequired(newgame, currentMD5)) {
            $rootScope.$broadcast('setupAlert', newgame);
          }
          if (twGameDetails.playerCanPlay(newgame, currentMD5)) {
            $rootScope.$broadcast('playAlert', newgame);
          }
          if (twGameDetails.playerChallengeResponseNeeded(newgame, currentMD5)) {
            $rootScope.$broadcast('challengedAlert', newgame);
          }
          $rootScope.$broadcast('phaseChangeAlert', newgame);
        }
      };
    }
  ]);
