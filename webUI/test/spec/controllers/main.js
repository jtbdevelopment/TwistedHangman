'use strict';

describe('Controller: MainCtrl', function () {

    beforeEach(module('twistedHangmanApp'));

    var MainCtrl, rootScope, scope, playerService, gameCache, player;
    var versionNotesService;
    var longName = 'long name';
    var logoutCalled;

    beforeEach(inject(function ($controller, $rootScope) {
        versionNotesService = {
            displayVersionNotesIfAppropriate: jasmine.createSpy('displayVersion')
        };
        scope = $rootScope.$new();
        rootScope = $rootScope;
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
            jtbBootstrapVersionNotesService: versionNotesService,
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
        expect(scope.adTemplate).toEqual('views/ads/empty.html');
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.currentPlayer).toEqual(player);
        expect(scope.playerGreeting).toEqual('Welcome XYZ');
        expect(scope.createRefreshEnabled).toEqual(true);
        expect(scope.showAdmin).toEqual(true);
        expect(scope.showLogout).toEqual(false);
        expect(scope.includeTemplate).toEqual('views/sidebar.html');
        expect(scope.adTemplate).toEqual('views/ads/ad-holder.html');
        expect(scope.appName).toEqual(longName);
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
        expect(scope.adTemplate).toEqual('views/ads/ad-holder.html');

        player = {displayName: 'ABC', md5: '6666', adminUser: false, source: 'MANUAL'};
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.currentPlayer).toEqual(player);
        expect(scope.currentPlayer.displayName).toEqual('ABC');
        expect(scope.playerGreeting).toEqual('Welcome ABC');
        expect(scope.showAdmin).toEqual(true);
        expect(scope.showLogout).toEqual(true);
        expect(scope.includeTemplate).toEqual('views/sidebar.html');
        expect(scope.adTemplate).toEqual('views/ads/ad-holder.html');
        expect(versionNotesService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(
            1.3,
            'Reorganized the game lists, more games per day and fewer ads.');
    });

    it('logout', function () {
        rootScope.$broadcast('playerLoaded');
        rootScope.$apply();
        expect(scope.includeTemplate).toEqual('views/sidebar.html');
        expect(scope.adTemplate).toEqual('views/ads/ad-holder.html');
        expect(logoutCalled).toEqual(false);
        scope.logout();
        expect(logoutCalled).toEqual(true);
        expect(scope.includeTemplate).toEqual('views/empty.html');
        expect(scope.adTemplate).toEqual('views/ads/empty.html');
    });

});
