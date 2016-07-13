'use strict';

describe('Service: gameAlerts', function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var rootScope, service;
    var md5value = 'md5';
    var player;
    var gameDetails = {};
    beforeEach(module(function ($provide) {
        $provide.factory('jtbPlayerService', function () {
            return {
                currentPlayer: function () {
                    return player;
                }
            };
        });
        $provide.factory('twGameDetails', function () {
            return gameDetails;
        });
    }));

    beforeEach(inject(function ($rootScope, $injector) {
        player = {md5: md5value};
        rootScope = $rootScope;
        spyOn(rootScope, '$broadcast').and.callThrough();
        service = $injector.get('twGameAlerts');
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
    }));

    it('initializes', function () {
        expect(service.md5()).toEqual(md5value);
    });

    it('listens for playerLoaded', function () {
        expect(service.md5()).toEqual(md5value);
        player.md5 = 'newmd5';
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(service.md5()).toEqual('newmd5');
    });

    describe('new game entry alerts', function () {
        afterEach(function () {
            delete gameDetails.playerChallengeResponseNeeded;
            delete gameDetails.playerCanPlay;
            delete gameDetails.playerSetupEntryRequired;
            delete gameDetails.gameEndForPlayer;
        });

        it('publishes new game alert - set 1', function () {
            var newgame = {id: 'x', stuff: 'here', gamePhase: 'Quit'};
            gameDetails.playerChallengeResponseNeeded = function () {
                return false;
            };
            gameDetails.playerCanPlay = function () {
                return true;
            };
            gameDetails.playerSetupEntryRequired = function () {
                return false;
            };
            gameDetails.gameEndForPlayer = function () {
                return 'Hung!';
            };
            rootScope.$broadcast('gameAdded', newgame);
            rootScope.$apply();

            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('roundOverAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('quitAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('hungAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('solvedAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('setupAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('playAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('challengedAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
        });

        it('publishes new game alert - set 2', function () {
            var newgame = {id: 'x', stuff: 'here'};
            gameDetails.playerChallengeResponseNeeded = function () {
                return true;
            };
            gameDetails.playerCanPlay = function () {
                return false;
            };
            gameDetails.playerSetupEntryRequired = function () {
                return true;
            };
            gameDetails.gameEndForPlayer = function () {
                return 'Solved!';
            };
            rootScope.$broadcast('gameAdded', newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('roundOverAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('quitAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('hungAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('solvedAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('setupAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('playAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('challengedAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
        });

        it('publishes new game alert - set 3', function () {
            var newgame = {id: 'x', stuff: 'here', gamePhase: 'RoundOver'};
            gameDetails.playerChallengeResponseNeeded = function () {
                return true;
            };
            gameDetails.playerCanPlay = function () {
                return false;
            };
            gameDetails.playerSetupEntryRequired = function () {
                return true;
            };
            gameDetails.gameEndForPlayer = function () {
                return 'X';
            };
            rootScope.$broadcast('gameAdded', newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('roundOverAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('quitAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('hungAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('solvedAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('setupAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('playAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('challengedAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
        });
    });

    describe('game update alerts', function () {
        var oldgame, newgame;
        beforeEach(function () {
            oldgame = {
                id: 'x',
                gamePhase: 'Setup'
            };

            newgame = angular.copy(oldgame);

            gameDetails.playerChallengeResponseNeeded = function () {
                return false;
            };

            gameDetails.playerCanPlay = function () {
                return false;
            };

            gameDetails.playerSetupEntryRequired = function () {
                return false;
            };

            gameDetails.gameEndForPlayer = function () {
                return '';
            };
        });

        afterEach(function () {
            delete gameDetails.playerChallengeResponseNeeded;
            delete gameDetails.playerCanPlay;
            delete gameDetails.playerSetupEntryRequired;
            delete gameDetails.gameEndForPlayer;
        });

        it('publishes alerts on phase change', function () {
            newgame.gamePhase = '';
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('phaseChangeAlert', newgame);
        });

        it('does not publish alert on non-phase change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('phaseChangeAlert', newgame);
        });

        it('publishes alert on game end change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('phaseChangeAlert', newgame);
        });

        it('publishes alert on game end change to solved', function () {
            gameDetails.gameEndForPlayer = function (game) {
                if (game === oldgame) {
                    return '';
                }
                return 'Solved!';
            };
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('solvedAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('hungAlert', newgame);
        });

        it('publishes alert on game end change to hung', function () {
            gameDetails.gameEndForPlayer = function (game) {
                if (game === oldgame) {
                    return '';
                }
                return 'Hung!';
            };
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('solvedAlert', newgame);
            expect(rootScope.$broadcast).toHaveBeenCalledWith('hungAlert', newgame);
        });

        it('does not publish alert on non game end change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('solvedAlert', newgame);
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('hungAlert', newgame);
        });

        it('publishes alert on challenge', function () {
            gameDetails.playerChallengeResponseNeeded = function (game) {
                return game !== oldgame;
            };
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('challengedAlert', newgame);
        });

        it('does not publish alert on non challenge change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('challengedAlert', newgame);
        });

        it('publishes alert on round over change', function () {
            newgame.gamePhase = 'RoundOver';
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('roundOverAlert', newgame);
        });

        it('does not publish alert on non round over change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('roundOverAlert', newgame);
        });

        it('publishes alert on quit change', function () {
            newgame.gamePhase = 'Quit';
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('quitAlert', newgame);
        });

        it('does not publish alert on non quit change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('quitAlert', newgame);
        });

        it('publishes alert on declined change', function () {
            newgame.gamePhase = 'Declined';
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('declinedAlert', newgame);
        });

        it('does not publish alert on non declined change', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('declinedAlert', newgame);
        });

        it('publishes alert on play', function () {
            gameDetails.playerCanPlay = function (game) {
                return game !== oldgame;
            };
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('playAlert', newgame);
        });

        it('does not publish alert on non play', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('playAlert', newgame);
        });

        it('publishes alert on setup', function () {
            gameDetails.playerSetupEntryRequired = function (game) {
                return game !== oldgame;
            };
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).toHaveBeenCalledWith('setupAlert', newgame);
        });

        it('does not publish alert on non setup', function () {
            rootScope.$broadcast('gameUpdated', oldgame, newgame);
            rootScope.$apply();
            expect(rootScope.$broadcast).not.toHaveBeenCalledWith('setupAlert', newgame);
        });

    });
});
