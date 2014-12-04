'use strict';

//  TODO - break this monster up
angular.module('twistedHangmanApp').factory('twShowGameService', ['$rootScope', 'twGamePhaseService', function ($rootScope, twGamePhaseService) {
  var LETTERA = 'A'.charCodeAt(0);

  function computeWordPhraseDisplay(scope) {
    if (angular.isUndefined(scope.gameState)) {
      //  TODO - test this case
      return;
    }
    scope.workingWordPhraseArray = scope.gameState.workingWordPhrase.split('');
    for (var i = 0; i < scope.workingWordPhraseArray.length; i++) {
      var r = 'regularwp';
      if (angular.isDefined(scope.gameState.featureData.ThievingPositionTracking)) {
        r = (scope.gameState.featureData.ThievingPositionTracking[i] === true) ?
          'stolenwp' :
          (scope.workingWordPhraseArray[i] === '_' ? 'stealablewp' : 'regularwp'); //  TODO - test this extra condition
      }
      scope.workingWordPhraseClasses[i] = r;
    }
  }

  function computeImage(scope) {
    if (angular.isUndefined(scope.gameState)) {
      //  TODO - test this case
      scope.image = 'hangman0.png';
      return;
    }
    if (scope.gameState.penalties === scope.gameState.maxPenalties) {
      scope.image = 'hangman13.png';
      return;
    }
    var n = 0;
    switch (scope.gameState.maxPenalties) {
      case 6:
      case 10:
        n = scope.gameState.penalties + 3;
        break;
      default:
        n = scope.gameState.penalties;
        break;
    }
    scope.image = 'hangman' + n + '.png';
  }

  function computeKeyboardDisplay(scope) {
    if (angular.isUndefined(scope.gameState)) {
      //  TODO - test case
      return;
    }
    scope.gameState.guessedLetters.forEach(function (item) {
      scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'guessedkb';
    });
    scope.gameState.badlyGuessedLetters.forEach(function (item) {
      scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'badguesskb';
    });
    if (angular.isDefined(scope.gameState.featureData.ThievingLetters)) {
      scope.gameState.featureData.ThievingLetters.forEach(function (item) {
        scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'stolenkb';
      });
    }
  }

  //  TODO - test
  function computeDescription(scope) {
    var t = '';
    if (scope.game.features.indexOf('Thieving') >= 0) {
      t = t + 'Thieving Allowed, ';
    }
    if (scope.game.features.indexOf('SystemPuzzles') >= 0) {
      t = t + 'Generated Puzzle, ';
    } else {
      if (scope.game.features.indexOf('AlternatingPuzzleSetter') >= 0) {
        t = t + 'Puzzle Set By ' + scope.game.players[scope.game.wordPhraseSetter] + ', ';
      } else {
        t = t + 'Head-2-Head Puzzles, ';
      }
    }
    if (scope.game.features.indexOf('SinglePlayer') < 0) {
      if (scope.game.features.indexOf('SingleWinner') >= 0) {
        t = t + 'Until First Solver, ';
      } else {
        t = t + 'Until All Finish, ';
      }
    }
    if (scope.game.features.indexOf('TurnBased') >= 0) {
      t = t + scope.game.players[scope.game.featureData.TurnBased] + '\'s Turn';
    } else {
      t = t + 'Live Play';
    }
    scope.generalInfo = t;
  }

  //  TODO - test this function
  function computeDisplayAreas(scope) {
    scope.showPlaySection = false;
    scope.allowPlayMoves = false;
    scope.showChallengeButtons = false;
    scope.showAcceptButton = false;
    scope.showPuzzleEnty = false;
    scope.allowPuzzleEntry = false;
    scope.showRematchButtons = false;
    scope.showQuitButton = false;
    if (angular.isUndefined(scope.game) ||
      angular.isUndefined(scope.player) ||
      (angular.isUndefined(scope.gameState) && scope.player.md5 !== scope.game.wordPhraseSetter)) {
      return;
    }
    scope.showPlaySection = (scope.game.wordPhraseSetter !== scope.player.md5);
    switch (scope.game.gamePhase) {
      case 'Challenged':
        scope.showChallengeButtons = true;
        if (scope.game.playerStates[scope.player.md5] === 'Pending') {
          scope.showAcceptButton = true;
        }
        break;
      case 'Setup':
        scope.showQuitButton = true;
        scope.showPuzzleEnty = true;
        if (scope.game.wordPhraseSetter === null) {
          angular.forEach(scope.game.solverStates, function (state, md5) {
            if (md5 !== scope.player.md5) {
              scope.allowPuzzleEntry = (state.wordPhrase === '');
            }
          });
          if (scope.allowPuzzleEntry === false) {
            angular.forEach(scope.game.players, function (name, md5) {
              if (md5 !== scope.player.md5) {
                scope.setPuzzleMessage = 'Waiting for ' + name + ' to set puzzle.';
              }
            });
          }
        } else {
          scope.allowPuzzleEntry = (scope.game.wordPhraseSetter === scope.player.md5);
          scope.setPuzzleMessage = 'Waiting for ' + scope.game.players[scope.game.wordPhraseSetter] + ' to set puzzle.';
        }
        break;
      case 'Playing':
        scope.showQuitButton = true;
        if (angular.isDefined(scope.gameState)) {
          if (scope.gameState.isPuzzleOver === false) {
            if (angular.isUndefined(scope.game.featureData.TurnBased)) {
              scope.allowPlayMoves = true;
            } else {
              scope.allowPlayMoves = (scope.player.md5 === scope.game.featureData.TurnBased);
            }
          }
        }
        break;
      case 'RoundOver':
        scope.showRematchButtons = true;
        break;
      case 'Declined':
      case 'Quit':
      case 'NextRoundStarted':
        break;
    }
  }

  function computeGameDisplay(scope) {
    if (angular.isUndefined(scope.player) || angular.isUndefined(scope.game)) {
      return;
    }
    scope.gameState = scope.game.solverStates[scope.player.md5];
    computeDisplayAreas(scope);
    computeImage(scope);
    computeWordPhraseDisplay(scope);
    computeKeyboardDisplay(scope);
    computeDescription(scope);
  }

  return {
    initializeScope: function (scope) {
      scope.workingWordPhraseArray = [];
      scope.workingWordPhraseClasses = [];
      scope.letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
      scope.letterClasses = [];
      scope.letters.forEach(function () {
        scope.letterClasses.push('regular');
      });
      computeDisplayAreas(scope);
    },

    processGame: function (scope, data) {
      scope.game = data;
      if (angular.isDefined(scope.game)) {
        twGamePhaseService.phases().then(function (phases) {
          scope.phaseDescription = phases[scope.game.gamePhase][0];
        }, function () {
          //  TODO
        });
        //  TODO - convert to millis on server
        scope.lastUpdate = new Date(scope.game.lastUpdate * 1000);
        scope.created = new Date(scope.game.created * 1000);

        //  TODO - test
        if (angular.isDefined(scope.game.declined) && scope.game.declined > 0) {
          scope.declined = new Date(scope.game.declined * 1000);
        } else {
          scope.declined = 'N/A';
        }
        if (angular.isDefined(scope.game.completed) && scope.game.completed > 0) {
          scope.completed = new Date(scope.game.completed * 1000);
        } else {
          scope.completed = 'N/A';
        }
        if (angular.isDefined(scope.game.rematched) && scope.game.rematched > 0) {
          scope.rematched = new Date(scope.game.rematched * 1000);
        } else {
          scope.rematched = 'N/A';
        }
      }

      computeGameDisplay(scope);
    },

    processUpdate: function (scope, data) {
      var beforePhase = scope.game.gamePhase;
      this.processGame(scope, data);
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      if (data.gamePhase !== beforePhase) {
        $rootScope.$broadcast('refreshGames', beforePhase);
      }
    }
  };

}]);

