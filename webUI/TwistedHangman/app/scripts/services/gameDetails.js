'use strict';

angular.module('twistedHangmanApp').factory('twGameDetails',
  function () {
    function checkParams(game, md5) {
      return !(angular.isUndefined(game) || angular.isUndefined(md5) || md5.trim() === '');
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

        var puzzle = game.solverStates[md5];
        return angular.isDefined(puzzle) && !puzzle.isPuzzleOver;
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
      },

      profileForPlayer: function (game, md5) {
        if (checkParams(game, md5)) {
          if (angular.isDefined(game.playerProfiles[md5])) {
            return game.playerProfiles[md5];
          }
        }
        return '';
      },
      imageForPlayer: function (game, md5) {
        if (checkParams(game, md5)) {
          if (angular.isDefined(game.playerImages[md5])) {
            return game.playerImages[md5];
          }
        }
        return null;
      },
      stateIconForPlayer: function (game, md5) {
        if (checkParams(game, md5)) {
          switch (game.playerStates[md5]) {
            case 'Quit':
              return 'flag';
            case 'Pending':
              return 'inbox';
            case 'Accepted':
              return 'thumbs-up';
            case 'Rejected':
              return 'thumbs-down';
          }
        }
        return 'question-sign';
      },
      roleIconForPlayer: function (game, md5) {
        switch (this.roleForPlayer(game, md5)) {
          case 'Set Puzzle':
            return 'eye-open';
          case 'Solver':
            return 'pencil';
        }
        return 'question-sign';
      },
      gameStateIconForPlayer: function (game, md5) {
        switch (this.gameEndForPlayer(game, md5)) {
          case 'Solved!':
            return 'ok';
          case 'Hung!':
            return 'remove';
          case 'Not Solved.':
            return 'search';
        }
        return 'question-sign';
      }
    };
  }
);
