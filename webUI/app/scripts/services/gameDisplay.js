'use strict';

angular.module('twistedHangmanApp').factory('twGameDisplay',
    ['$rootScope', 'jtbGamePhaseService', 'twGameDetails',
        function ($rootScope, jtbGamePhaseService, twGameDetails) {
            var LETTER_A = 'A'.charCodeAt(0);

            //  this map allows image compression rewrite to change names
            var imageMap = {
                0: '/images/hangman0.png',
                1: '/images/hangman1.png',
                2: '/images/hangman2.png',
                3: '/images/hangman3.png',
                4: '/images/hangman4.png',
                5: '/images/hangman5.png',
                6: '/images/hangman6.png',
                7: '/images/hangman7.png',
                8: '/images/hangman8.png',
                9: '/images/hangman9.png',
                10: '/images/hangman10.png',
                11: '/images/hangman11.png',
                12: '/images/hangman12.png',
                13: '/images/hangman13.png'
            };

            function computeImage(scope) {
                var n;
                if (angular.isUndefined(scope.gameState)) {
                    n = 0;
                } else if (scope.gameState.penalties === scope.gameState.maxPenalties) {
                    n = 13;
                } else {
                    n = 0;
                    switch (scope.gameState.maxPenalties) {
                        case 6:
                        case 10:
                            n = scope.gameState.penalties + 3;
                            break;
                        default:
                            n = scope.gameState.penalties;
                            break;
                    }
                }
                scope.image = imageMap[n];
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
                                    (scope.workingWordPhraseArray[i] === '_' && scope.gameState.penaltiesRemaining > 1) ?
                                        'stealablewp' : 'regularwp'
                                );
                        }
                        scope.workingWordPhraseClasses[i] = r;
                    }
                }
            }

            function computeKeyboardDisplay(scope) {
                if (angular.isDefined(scope.gameState)) {
                    scope.gameState.guessedLetters.forEach(function (item) {
                        scope.letterClasses[item.charCodeAt(0) - LETTER_A] = 'guessedkb';
                    });
                    scope.gameState.badlyGuessedLetters.forEach(function (item) {
                        scope.letterClasses[item.charCodeAt(0) - LETTER_A] = 'badguesskb';
                    });
                    if (angular.isDefined(scope.gameState.featureData.ThievingLetters)) {
                        scope.gameState.featureData.ThievingLetters.forEach(function (item) {
                            scope.letterClasses[item.charCodeAt(0) - LETTER_A] = 'stolenkb';
                        });
                    }
                }
            }

            function computeDescription(scope) {
                scope.generalInfo = twGameDetails.gameDescription(scope.game);
            }

            function computeDisplayAreas(scope) {
                scope.showPlaySection = false;
                scope.allowPlayMoves = false;
                scope.showChallengeButtons = false;
                scope.showAcceptButton = false;
                scope.showPuzzleEnty = false;
                scope.allowPuzzleEntry = false;
                scope.showRematchButtons = false;
                scope.showQuitButton = false;
                scope.setPuzzleMessage = '';
                if (angular.isDefined(scope.game) && angular.isDefined(scope.player)) {
                    var md5 = scope.player.md5;
                    scope.showPlaySection = !twGameDetails.playerIsSetter(scope.game, md5);
                    switch (scope.game.gamePhase) {
                        case 'Challenged':
                            scope.showChallengeButtons = true;
                            scope.showAcceptButton = twGameDetails.playerChallengeResponseNeeded(scope.game, md5);
                            break;
                        case 'Setup':
                            scope.showQuitButton = true;
                            scope.showPuzzleEnty = true;
                            scope.allowPuzzleEntry = twGameDetails.playerSetupEntryRequired(scope.game, md5);
                            if (scope.game.wordPhraseSetter === null) {
                                if (scope.allowPuzzleEntry === false) {
                                    angular.forEach(scope.game.players, function (name, md5) {
                                        if (md5 !== scope.player.md5) {
                                            scope.setPuzzleMessage = 'Waiting for ' + name + ' to set puzzle.';
                                        }
                                    });
                                }
                            } else {
                                scope.setPuzzleMessage = 'Waiting for ' + scope.game.players[scope.game.wordPhraseSetter] + ' to set puzzle.';
                            }
                            break;
                        case 'Playing':
                            scope.showQuitButton = true;
                            scope.allowPlayMoves = twGameDetails.playerCanPlay(scope.game, md5);
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
                        jtbGamePhaseService.phases().then(function (phases) {
                            scope.phaseDescription = phases[scope.game.gamePhase][0];
                        });

                        scope.lastUpdate = parseDate(scope.game.lastUpdate);
                        scope.created = parseDate(scope.game.created);
                        scope.declined = parseDate(scope.game.declinedTimestamp);
                        scope.completed = parseDate(scope.game.completedTimestamp);
                        scope.rematched = parseDate(scope.game.rematchTimestamp);
                    }

                    computeGameDisplay(scope);
                },

                processGameUpdateForScope: function (scope, game) {
                    this.updateScopeForGame(scope, game);
                }
            };

        }]);

