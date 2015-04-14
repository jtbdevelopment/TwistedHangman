'use strict';

angular.module('twistedHangmanApp').factory('twGameAlerts',
  ['$rootScope', 'twGameDetails', 'jtbPlayerService',
    function ($rootScope, twGameDetails, jtbPlayerService) {
      var currentMD5;

      function loadMD5() {
        currentMD5 = jtbPlayerService.currentPlayer().md5;
      }

      $rootScope.$on('playerLoaded', function () {
        loadMD5();
      });

      $rootScope.$on('gameAdded', function (event, newgame) {
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
      });

      $rootScope.$on('gameUpdated', function (event, oldgame, newgame) {
        if (newgame.gamePhase === 'RoundOver' && oldgame.gamePhase !== 'RoundOver') {
          $rootScope.$broadcast('roundOverAlert', newgame);
        }
        if (newgame.gamePhase === 'Declined' && oldgame.gamePhase !== 'Declined') {
          $rootScope.$broadcast('declinedAlert', newgame);
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
        if (twGameDetails.playerSetupEntryRequired(newgame, currentMD5) && !twGameDetails.playerSetupEntryRequired(oldgame, currentMD5)) {
          $rootScope.$broadcast('setupAlert', newgame);
        }
        if (twGameDetails.playerCanPlay(newgame, currentMD5) && !twGameDetails.playerCanPlay(oldgame, currentMD5)) {
          $rootScope.$broadcast('playAlert', newgame);
        }
        if (twGameDetails.playerChallengeResponseNeeded(newgame, currentMD5) && !twGameDetails.playerChallengeResponseNeeded(oldgame, currentMD5)) {
          $rootScope.$broadcast('challengedAlert', newgame);
        }
        if (oldgame.gamePhase !== newgame.gamePhase) {
          $rootScope.$broadcast('phaseChangeAlert', newgame);
        }
      });

      return {
        md5: function () {
          return currentMD5;
        }
      };
    }
  ]);
