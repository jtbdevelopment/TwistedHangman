'use strict';

describe('Service: showGameSevice', function () {
    beforeEach(module('twistedHangmanApp'));

    var md1SS = {
        isPuzzleOver: true,
        isPuzzleSolved: true,
        workingWordPhrase: '__ A_',
        maxPenalties: 6,
        penalties: 2,
        penaltiesRemaining: 4,
        badlyGuessedLetters: ['X', 'Y'],
        guessedLetters: ['A', 'X', 'Y'],
        featureData: {}
    };
    var testGame = {
        id: 'id2',
        players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
        wordPhraseSetter: 'md4',
        gamePhase: 'Playing',
        solverStates: {
            md1: md1SS,
            md2: {isPuzzleOver: true, isPuzzleSolved: false},
            md3: {isPuzzleOver: false, isPuzzleSolved: false}
        },
        playerRunningScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5},
        gameID: 'id',
        featureData: {},
        features: []
    };
    var testPlayer = {
        id: 'pid',
        md5: 'md1'
    };

    var service, rootscope, scope, twGameDetails, phaseDeferred;

    describe('using real game details ', function () {
        beforeEach(module(function ($provide) {
            $provide.factory('jtbGamePhaseService', ['$q', function ($q) {
                return {
                    phases: function () {
                        phaseDeferred = $q.defer();
                        return phaseDeferred.promise;
                    }
                };
            }]);
        }));

        beforeEach(inject(function ($rootScope, $injector) {
            rootscope = $rootScope;
            scope = rootscope.$new();
            spyOn($rootScope, '$broadcast').and.callThrough();
            service = $injector.get('twGameDisplay');
        }));

        it('initialize scope', function () {
            scope.game = testGame;
            service.initializeScope(scope);
            expect(scope.workingWordPhraseArray).toEqual([]);
            expect(scope.workingWordPhraseClasses).toEqual([]);
            expect(scope.letters).toEqual(['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']);
            expect(scope.letterClasses).toEqual(['regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular']);
        });

        it('computeState with phase description', function () {
            service.updateScopeForGame(scope, testGame);
            phaseDeferred.resolve({something: ['X', 'Y'], Playing: ['havefun', 'ornot']});
            rootscope.$apply();
            expect(scope.phaseDescription).toEqual('havefun');
        });

        it('computeState without player or game does not work', function () {
            service.updateScopeForGame(scope);
            expect(scope.gameState).toBeUndefined();
        });

        it('computeState without player does not work', function () {
            service.updateScopeForGame(scope, testGame);
            expect(scope.gameState).toBeUndefined();
        });

        it('computeState without game does not work', function () {
            scope.player = testPlayer;
            service.updateScopeForGame(scope);
            expect(scope.gameState).toBeUndefined();
        });

        describe('Post Initialization Tests', function () {
            beforeEach(function () {
                scope.player = angular.copy(testPlayer);
                service.initializeScope(scope);
            });

            it('computeState for non-thieving game', function () {
                service.updateScopeForGame(scope, testGame);
                expect(scope.gameState).toBe(md1SS);
                expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
                expect(scope.image).toEqual('hangman5.png');
                expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
            });

            it('computeState image for no gameState', function () {
                scope.player.md5 = 'XXX';
                var game = angular.copy(testGame);
                game.solverStates.md1.maxPenalties = 13;
                delete scope.gameState;
                service.updateScopeForGame(scope, game);
                expect(scope.image).toEqual('hangman0.png');
            });

            it('computeState image for diff max penalties', function () {
                var game = angular.copy(testGame);
                game.solverStates.md1.maxPenalties = 13;
                service.updateScopeForGame(scope, game);
                expect(scope.image).toEqual('hangman2.png');
                game.solverStates.md1.maxPenalties = 10;
                service.updateScopeForGame(scope, game);
                expect(scope.image).toEqual('hangman5.png');
                game.solverStates.md1.maxPenalties = 9;
                service.updateScopeForGame(scope, game);
                expect(scope.image).toEqual('hangman2.png');
                game.solverStates.md1.maxPenalties = 2;
                service.updateScopeForGame(scope, game);
                expect(scope.image).toEqual('hangman13.png');
            });

            it('computeState for thieving game', function () {
                var game = angular.copy(testGame);
                game.solverStates.md1.featureData.ThievingPositionTracking = [false, true, false, false, true];
                game.solverStates.md1.featureData.ThievingLetters = ['B'];
                service.updateScopeForGame(scope, game);
                expect(scope.workingWordPhraseClasses).toEqual(['stealablewp', 'stolenwp', 'regularwp', 'regularwp', 'stolenwp']);
                expect(scope.image).toEqual('hangman5.png');
                expect(scope.letterClasses).toEqual(['guessedkb', 'stolenkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
            });

            it('computeState for thieving game on last penalty', function () {
                var game = angular.copy(testGame);
                game.solverStates.md1.featureData.ThievingPositionTracking = [false, true, false, false, true];
                game.solverStates.md1.featureData.ThievingLetters = ['B'];
                game.solverStates.md1.penalties = 5;
                game.solverStates.md1.penaltiesRemaining = 1;
                service.updateScopeForGame(scope, game);
                expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'stolenwp', 'regularwp', 'regularwp', 'stolenwp']);
                expect(scope.image).toEqual('hangman8.png');
                expect(scope.letterClasses).toEqual(['guessedkb', 'stolenkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'regular']);
            });

            it('updateScopeForGame with new copy', function () {
                var game = angular.copy(testGame);
                service.updateScopeForGame(scope, game);

                var update = angular.copy(testGame);
                update.lastUpdate = 1345100;
                update.created = 1345000;
                update.solverStates.md1.penalties = 3;
                update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];

                service.updateScopeForGame(scope, update);
                expect(scope.lastUpdate).toEqual(new Date(1345100));
                expect(scope.created).toEqual(new Date(1345000));
                expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
                expect(scope.image).toEqual('hangman6.png');
                expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
            });

            it('processGameUpdateForScope calls cache to update', function () {
                var game = angular.copy(testGame);
                service.updateScopeForGame(scope, game);

                var update = angular.copy(testGame);
                update.lastUpdate = 1345100;
                update.created = 1345000;
                update.solverStates.md1.penalties = 3;
                update.solverStates.md1.badlyGuessedLetters = ['X', 'Y', 'Z'];

                service.processGameUpdateForScope(scope, update);
                expect(scope.lastUpdate).toEqual(new Date(1345100));
                expect(scope.created).toEqual(new Date(1345000));
                expect(scope.workingWordPhraseClasses).toEqual(['regularwp', 'regularwp', 'regularwp', 'regularwp', 'regularwp']);
                expect(scope.image).toEqual('hangman6.png');
                expect(scope.letterClasses).toEqual(['guessedkb', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'regular', 'badguesskb', 'badguesskb', 'badguesskb']);
            });

        });
    });

    describe('using mock game details ', function () {
        var game;
        beforeEach(module(function ($provide) {
            twGameDetails = {
                playerIsSetter: function () {
                    return false;
                },
                playerCanPlay: function () {
                    return false;
                },
                gameDescription: function () {
                    return 'Show Me';
                }
            };
            $provide.factory('twGameDetails', function () {
                return twGameDetails;
            });
        }));

        beforeEach(inject(function ($rootScope, $injector) {
            rootscope = $rootScope;
            scope = rootscope.$new();
            spyOn($rootScope, '$broadcast');
            service = $injector.get('twGameDisplay');
            game = angular.copy(testGame);
            scope.game = game;
            scope.player = testPlayer;
            service.initializeScope(scope);
        }));

        describe('testing the description function', function () {
            it('test value is what is returned', function () {
                game.features = ['Thieving', 'TurnBased', 'SystemPuzzles', 'SinglePlayer', 'SingleWinner'];
                game.featureData.TurnBased = 'md2';
                service.updateScopeForGame(scope, game);
                expect(scope.generalInfo).toEqual('Show Me');
            });
        });

        describe('testing the show function', function () {
            it('show play section', function () {
                twGameDetails.playerIsSetter = function () {
                    return true;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.showPlaySection).toEqual(false);
                twGameDetails.playerIsSetter = function () {
                    return false;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.showPlaySection).toEqual(true);
            });

            it('show challenge buttons', function () {
                game.gamePhase = 'Challenged';
                twGameDetails.playerChallengeResponseNeeded = function () {
                    return true;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(true);
                expect(scope.showAcceptButton).toEqual(true);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');

                twGameDetails.playerChallengeResponseNeeded = function () {
                    return false;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(true);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');
            });

            it('show play buttons', function () {
                game.gamePhase = 'Playing';
                twGameDetails.playerCanPlay = function () {
                    return true;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(true);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(true);
                expect(scope.setPuzzleMessage).toEqual('');

                twGameDetails.playerCanPlay = function () {
                    return false;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(true);
                expect(scope.setPuzzleMessage).toEqual('');
            });

            it('show round over buttons', function () {
                game.gamePhase = 'RoundOver';
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(true);
                expect(scope.showQuitButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');
            });

            it('show quit buttons', function () {
                game.gamePhase = 'Quit';
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');
            });

            it('show declined buttons', function () {
                game.gamePhase = 'Declined';
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');
            });

            it('show rematched buttons', function () {
                game.gamePhase = 'NextRoundStarted';
                service.updateScopeForGame(scope, game);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(false);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');
            });

            it('show setup buttons head 2 head', function () {
                game.gamePhase = 'Setup';
                game.wordPhraseSetter = null;
                twGameDetails.playerSetupEntryRequired = function () {
                    return true;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(true);
                expect(scope.allowPuzzleEntry).toEqual(true);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(true);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('');

                twGameDetails.playerSetupEntryRequired = function () {
                    return false;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(true);
                expect(scope.allowPuzzleEntry).toEqual(false);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(true);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('Waiting for P5 to set puzzle.');
            });

            it('show setup buttons head 2 head', function () {
                game.gamePhase = 'Setup';
                game.wordPhraseSetter = 'md4';
                twGameDetails.playerSetupEntryRequired = function () {
                    return true;
                };
                service.updateScopeForGame(scope, game);
                expect(scope.allowPlayMoves).toEqual(false);
                expect(scope.showPuzzleEnty).toEqual(true);
                expect(scope.allowPuzzleEntry).toEqual(true);
                expect(scope.showRematchButtons).toEqual(false);
                expect(scope.showQuitButton).toEqual(true);
                expect(scope.showChallengeButtons).toEqual(false);
                expect(scope.showAcceptButton).toEqual(false);
                expect(scope.setPuzzleMessage).toEqual('Waiting for P4 to set puzzle.');
            });
        });
    });
});

