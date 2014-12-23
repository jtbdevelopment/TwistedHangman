'use strict';

//  TODO - break this monster up
angular.module('twistedHangmanApp').factory('twGameDisplay',
  ['$rootScope', '$location', 'twGamePhaseService', 'twGameCache',
    function ($rootScope, $location, twGamePhaseService, twGameCache) {
      var LETTERA = 'A'.charCodeAt(0);

      function computeImage(scope) {
        if (angular.isUndefined(scope.gameState)) {
          scope.image = 'hangman0.png';
        } else if (scope.gameState.penalties === scope.gameState.maxPenalties) {
          scope.image = 'hangman13.png';
        } else {
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
      }

      function computeWordPhraseDisplay(scope) {
        if (angular.isDefined(scope.gameState)) {
          scope.workingWordPhraseArray = scope.gameState.workingWordPhrase.split('');
          for (var i = 0; i < scope.workingWordPhraseArray.length; i++) {
            var r = 'regularwp';
            if (angular.isDefined(scope.gameState.featureData.ThievingPositionTracking)) {
              r = (scope.gameState.featureData.ThievingPositionTracking[i] === true) ?
                'stolenwp' :
                (
                  (scope.workingWordPhraseArray[i] === '_' &&
                    //  TODO - test this extra condition
                  scope.gameState.penaltiesRemaining > 1) ? 'stealablewp' : 'regularwp');
            }
            scope.workingWordPhraseClasses[i] = r;
          }
        }
      }

      function computeKeyboardDisplay(scope) {
        if (angular.isDefined(scope.gameState)) {
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
      //  TODO - overlap with gameDetails
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
        if (angular.isDefined(scope.player) && angular.isDefined(scope.game)) {
          scope.gameState = scope.game.solverStates[scope.player.md5];
          computeDisplayAreas(scope);
          computeImage(scope);
          computeWordPhraseDisplay(scope);
          computeKeyboardDisplay(scope);
          computeDescription(scope);
        }
      }

      function parseDate(field) {
        if (angular.isDefined(field) && field > 0) {
          return new Date(field);
        } else {
          return 'N/A';
        }
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

        updateScopeForGame: function (scope, game) {
          scope.game = game;
          if (angular.isDefined(scope.game)) {
            twGamePhaseService.phases().then(function (phases) {
              scope.phaseDescription = phases[scope.game.gamePhase][0];
            }, function () {
              //  TODO
            });

            scope.lastUpdate = parseDate(scope.game.lastUpdate);
            scope.created = parseDate(scope.game.created);
            scope.declined = parseDate(scope.game.declined);
            scope.completed = parseDate(scope.game.declined);
            scope.rematched = parseDate(scope.game.declined);
          }

          computeGameDisplay(scope);
        },

        processGameUpdateForScope: function (scope, game) {
          this.updateScopeForGame(scope, game);
          twGameCache.putUpdatedGame(game);
        }
      };

    }]);

