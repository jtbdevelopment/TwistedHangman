'use strict';

angular.module('twistedHangmanApp').factory('twGameDetails',
  function () {
    return {
      roleForPlayer: function (game, md5) {
        if (angular.isUndefined(game)) {
          return '';
        }
        if (md5 === game.wordPhraseSetter) {
          return 'Set Puzzle';
        }
        return 'Solver';
      },

      gameEndForPlayer: function (game, md5) {
        if (angular.isUndefined(game)) {
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
        if (angular.isUndefined(game)) {
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
        if (angular.isUndefined(game)) {
          return '';
        }
        return game.playerRoundScores[md5];
      },

      runningScoreForPlayer: function (game, md5) {
        if (angular.isUndefined(game)) {
          return '';
        }
        return game.playerRunningScores[md5];
      }
    };
  }
);
