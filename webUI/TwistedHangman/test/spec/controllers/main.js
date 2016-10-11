'use strict';

describe('Controller: MainCtrl', function () {

    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var MainCtrl, rootScope, scope, playerService, gameCache, location, timeout, player, gameDetails;
    var longName = 'long name';
    var logoutCalled;
    var gameAlerts = {};

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope, $timeout, $location) {
        scope = $rootScope.$new();
        rootScope = $rootScope;
        timeout = $timeout;
        location = $location;
        gameDetails = {};
        spyOn(rootScope, '$broadcast').and.callThrough();
        logoutCalled = false;
        gameCache = {};
        playerService = {
            currentPlayer: function () {
                return player;
            },
            signOutAndRedirect: function () {
                logoutCalled = true;
            }
        };
        player = {displayName: 'XYZ', md5: '1234', adminUser: true};
        MainCtrl = $controller('MainCtrl', {
            $scope: scope,
            $location: location,
            jtbAppLongName: longName,
            jtbPlayerService: playerService,
            jtbGameCache: gameCache,
            twGameDetails: gameDetails,
            twGameAlerts: gameAlerts
        });
    }));

    it('initializes', function () {
        expect(scope.playerGreeting).toEqual('');
        expect(scope.createRefreshEnabled).toEqual(false);
        expect(scope.showAdmin).toEqual(false);
        expect(scope.showLogout).toEqual(false);
        expect(scope.currentPlayer).toEqual({gameSpecificPlayerAttributes: {freeGamesUsedToday: 0}});
        expect(scope.includeTemplate).toEqual('views/empty.html');
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.currentPlayer).toEqual(player);
        expect(scope.playerGreeting).toEqual('Welcome XYZ');
        expect(scope.alerts).toEqual([]);
        expect(scope.createRefreshEnabled).toEqual(true);
        expect(scope.showAdmin).toEqual(true);
        expect(scope.showLogout).toEqual(false);
        expect(scope.includeTemplate).toEqual('views/sidebar.html');
        expect(scope.appName).toEqual(longName);
    });

    it('test refresh button', function () {
        scope.refreshGames();
        expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', '');
    });

    it('refreshes on "playerLoaded" broadcast', function () {
        expect(scope.playerGreeting).toEqual('');
        expect(scope.includeTemplate).toEqual('views/empty.html');
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.playerGreeting).toEqual('Welcome XYZ');
        expect(scope.alerts).toEqual([]);
        expect(scope.showAdmin).toEqual(true);
        expect(scope.showLogout).toEqual(false);
        expect(scope.includeTemplate).toEqual('views/sidebar.html');

        scope.alerts.push('x');
        player = {displayName: 'ABC', md5: '6666', adminUser: false, source: 'MANUAL'};
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.currentPlayer).toEqual(player);
        expect(scope.currentPlayer.displayName).toEqual('ABC');
        expect(scope.playerGreeting).toEqual('Welcome ABC');
        expect(scope.alerts).toEqual([]);
        expect(scope.showAdmin).toEqual(true);
        expect(scope.showLogout).toEqual(true);
        expect(scope.includeTemplate).toEqual('views/sidebar.html');
    });

    it('logout', function () {
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.includeTemplate).toEqual('views/sidebar.html');
        expect(logoutCalled).toEqual(false);
        scope.logout();
        expect(logoutCalled).toEqual(true);
        expect(scope.includeTemplate).toEqual('views/empty.html');
    });

    describe('go to game', function () {
        beforeEach(function () {
            scope.alerts = [{id: '1'}, {id: '2'}, {id: '3'}];
            spyOn(location, 'path');
        });

        it('negative index', function () {
            scope.goToGame(-1);
            expect(location.path).not.toHaveBeenCalled();
            expect(scope.alerts).toEqual([{id: '1'}, {id: '2'}, {id: '3'}]);
        });

        it('too high index', function () {
            scope.goToGame(3);
            expect(location.path).not.toHaveBeenCalled();
            expect(scope.alerts).toEqual([{id: '1'}, {id: '2'}, {id: '3'}]);

            scope.goToGame(5);
            expect(location.path).not.toHaveBeenCalled();
            expect(scope.alerts).toEqual([{id: '1'}, {id: '2'}, {id: '3'}]);
        });

        it('hit an index', function () {
            scope.goToGame(1);
            expect(location.path).toHaveBeenCalledWith('/show/2');
            expect(scope.alerts).toEqual([{id: '1'}, {id: '3'}]);
        });
    });
    describe('process alert messages', function () {
        var game, preMessage;
        beforeEach(function () {
            rootScope.$broadcast('playerLoaded');
            rootScope.$apply();
            game = {id: 'ZZ'};
            preMessage = {id: 'Z', message: 'initial alert'};
            scope.alerts = [preMessage];
        });

        describe('when player is not watching game', function () {
            beforeEach(function () {
                spyOn(location, 'path').and.callFake(function () {
                    return '/show/Z';
                });
            });

            it('quitAlert not watching', function () {
                rootScope.$broadcast('quitAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'A game was quit!'}, preMessage]);
            });

            it('declinedAlert not watching', function () {
                rootScope.$broadcast('declinedAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'A challenge was declined!'}, preMessage]);
            });

            it('challengedAlert not watching', function () {
                rootScope.$broadcast('challengedAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'You\'ve been challenged!'}, preMessage]);
            });

            it('setupAlert not watching', function () {
                rootScope.$broadcast('setupAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'You need to enter a puzzle!'}, preMessage]);
            });

            it('play live not watching', function () {
                game.features = ['Other'];
                rootScope.$broadcast('playAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'Time to play!'}, preMessage]);
            });

            it('play turn based not watching', function () {
                game.features = ['TurnBased'];
                rootScope.$broadcast('playAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'It\'s you\'re turn!'}, preMessage]);
            });

            it('round over win not watching', function () {
                angular.extend(gameDetails, {
                    gameScoreForPlayer: function (g, md5) {
                        expect(g).toBe(game);
                        expect(md5).toEqual('1234');
                        return 1;
                    }
                });
                rootScope.$broadcast('roundOverAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'Round ended and you scored!'}, preMessage]);
            });

            it('round over draw not watching', function () {
                angular.extend(gameDetails, {
                    gameScoreForPlayer: function (g, md5) {
                        expect(g).toBe(game);
                        expect(md5).toEqual('1234');
                        return 0;
                    }
                });
                rootScope.$broadcast('roundOverAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'Round ended and you drew.'}, preMessage]);
            });

            it('round over lost not watching', function () {
                angular.extend(gameDetails, {
                    gameScoreForPlayer: function (g, md5) {
                        expect(g).toBe(game);
                        expect(md5).toEqual('1234');
                        return -2;
                    }
                });
                rootScope.$broadcast('roundOverAlert', game);
                timeout.flush();
                expect(scope.alerts).toEqual([{id: 'ZZ', message: 'Round ended and you lost!'}, preMessage]);
            });
        });

        describe('when player is watching game', function () {
            beforeEach(function () {
                spyOn(location, 'path').and.callFake(function () {
                    return '/show/ZZ';
                });
            });

            afterEach(function () {
                expect(scope.alerts).toEqual([preMessage]);
            });

            it('quitAlert not watching', function () {
                rootScope.$broadcast('quitAlert', game);
                timeout.flush();
            });

            it('declinedAlert not watching', function () {
                rootScope.$broadcast('declinedAlert', game);
                timeout.flush();
            });

            it('challengedAlert not watching', function () {
                rootScope.$broadcast('challengedAlert', game);
                timeout.flush();
            });

            it('setupAlert not watching', function () {
                rootScope.$broadcast('setupAlert', game);
                timeout.flush();
            });

            it('play live not watching', function () {
                game.features = ['Other'];
                rootScope.$broadcast('playAlert', game);
                timeout.flush();
            });

            it('play turn based not watching', function () {
                game.features = ['TurnBased'];
                rootScope.$broadcast('playAlert', game);
                timeout.flush();
            });

            it('round over win not watching', function () {
                angular.extend(gameDetails, {
                    gameScoreForPlayer: function (g, md5) {
                        expect(g).toBe(game);
                        expect(md5).toEqual('1234');
                        return 1;
                    }
                });
                rootScope.$broadcast('roundOverAlert', game);
                timeout.flush();
            });

            it('round over draw not watching', function () {
                angular.extend(gameDetails, {
                    gameScoreForPlayer: function (g, md5) {
                        expect(g).toBe(game);
                        expect(md5).toEqual('1234');
                        return 0;
                    }
                });
                rootScope.$broadcast('roundOverAlert', game);
                timeout.flush();
            });

            it('round over lost not watching', function () {
                angular.extend(gameDetails, {
                    gameScoreForPlayer: function (g, md5) {
                        expect(g).toBe(game);
                        expect(md5).toEqual('1234');
                        return -2;
                    }
                });
                rootScope.$broadcast('roundOverAlert', game);
                timeout.flush();
            });
        });
    });
});
