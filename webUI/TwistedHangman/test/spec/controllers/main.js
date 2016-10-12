'use strict';

describe('Controller: MainCtrl', function () {

    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var MainCtrl, rootScope, scope, playerService, gameCache, player;
    var longName = 'long name';
    var logoutCalled;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        rootScope = $rootScope;
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
            jtbAppLongName: longName,
            jtbPlayerService: playerService,
            jtbGameCache: gameCache
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
        expect(scope.showAdmin).toEqual(true);
        expect(scope.showLogout).toEqual(false);
        expect(scope.includeTemplate).toEqual('views/sidebar.html');

        player = {displayName: 'ABC', md5: '6666', adminUser: false, source: 'MANUAL'};
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.currentPlayer).toEqual(player);
        expect(scope.currentPlayer.displayName).toEqual('ABC');
        expect(scope.playerGreeting).toEqual('Welcome ABC');
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

});
