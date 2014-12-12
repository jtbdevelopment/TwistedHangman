'use strict';

angular.module('twistedHangmanApp').factory('twGameDetails',
  function () {
    function checkParams(game, md5) {
      if (angular.isUndefined(game) || angular.isUndefined(md5) || md5.trim() === '') {
        return false;
      }
      return true;
    }

    return {
      playerChallengeResponseNeeded: function (game, md5) {
        if (!checkParams(game, md5)) {
          return false;
        }

        if (game.gamePhase !== 'Challenged') {
          return false;
        }

        return game.playerStates[md5] === 'Pending';
      },
      playerCanPlay: function (game, md5) {
        if (!checkParams(game, md5)) {
          return false;
        }

        if (game.gamePhase !== 'Playing') {
          return false;
        }

        if (game.wordPhraseSetter === md5) {
          return false;
        }

        if (game.features.indexOf('TurnBased') >= 0) {
          return md5 === game.featureData.TurnBased;
        }
        return true;
      },

      playerIsSetter: function (game, md5) {
        if (angular.isUndefined(game)) {
          return false;
        }
        return md5 === game.wordPhraseSetter;
      },
      playerSetupEntryRequired: function (game, md5) {
        if (!checkParams(game, md5)) {
          return false;
        }

        if (game.gamePhase !== 'Setup') {
          return false;
        }

        var ret = false;
        if (game.wordPhraseSetter === null) {

          angular.forEach(game.solverStates, function (state, stateMd5) {
            if (stateMd5 !== md5) {
              ret = (state.wordPhrase === '');
            }
          });
          return ret;
        } else {
          return game.wordPhraseSetter === md5;
        }
      },

      roleForPlayer: function (game, md5) {
        if (!checkParams(game, md5)) {
          return '';
        }
        return this.playerIsSetter(game, md5) ? 'Set Puzzle' : 'Solver';
      },


      gameEndForPlayer: function (game, md5) {
        if (!checkParams(game, md5)) {
          return '';
        }

        if (md5 === game.wordPhraseSetter) {
          return 'N/A';
        }

        var solverState = game.solverStates[md5];
        if (angular.isUndefined(solverState)) {
          return 'Unknown';
        }
        return solverState.isPuzzleOver ? (solverState.isPuzzleSolved ? 'Solved!' : 'Hung!') : 'Not Solved.';
      },

      stateForPlayer: function (game, md5, field) {
        if (!checkParams(game, md5)) {
          return '';
        }

        if (md5 === game.wordPhraseSetter) {
          return 'N/A';
        }

        var solverState = game.solverStates[md5];
        if (angular.isUndefined(solverState)) {
          return 'Unknown';
        }

        return solverState[field];
      },

      gameScoreForPlayer: function (game, md5) {
        if (!checkParams(game, md5)) {
          return '';
        }

        return game.playerRoundScores[md5];
      },

      runningScoreForPlayer: function (game, md5) {
        if (!checkParams(game, md5)) {
          return '';
        }

        return game.playerRunningScores[md5];
      }
    };
  }
);
