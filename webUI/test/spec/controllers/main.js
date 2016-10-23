'use strict';

describe('Controller: MainCtrl', function () {

    beforeEach(module('twistedHangmanApp'));

    var MainCtrl, $rootScope, playerService, gameCache, player;
    var versionNotesService;
    var longName = 'long name';
    var logoutCalled;

    beforeEach(inject(function ($controller, _$rootScope_) {
        versionNotesService = {
            displayVersionNotesIfAppropriate: jasmine.createSpy('displayVersion')
        };
        $rootScope = _$rootScope_;
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
            jtbAppLongName: longName,
            jtbPlayerService: playerService,
            jtbBootstrapVersionNotesService: versionNotesService,
            jtbGameCache: gameCache
        });
    }));

    it('initializes', function () {
        expect(MainCtrl.playerGreeting).toEqual('');
        expect(MainCtrl.createRefreshEnabled).toEqual(false);
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.currentPlayer).toEqual({gameSpecificPlayerAttributes: {freeGamesUsedToday: 0}});
        expect(MainCtrl.includeTemplate).toEqual('views/empty.html');
        expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
        expect(MainCtrl.currentPlayer).toEqual(player);
        expect(MainCtrl.playerGreeting).toEqual('Welcome XYZ');
        expect(MainCtrl.createRefreshEnabled).toEqual(true);
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.includeTemplate).toEqual('views/sidebar.html');
        expect(MainCtrl.adTemplate).toEqual('views/ads/ad-holder.html');
        expect(MainCtrl.appName).toEqual(longName);
    });


    it('refreshes on "playerLoaded" broadcast', function () {
        expect(MainCtrl.playerGreeting).toEqual('');
        expect(MainCtrl.includeTemplate).toEqual('views/empty.html');
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
        expect(MainCtrl.playerGreeting).toEqual('Welcome XYZ');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.includeTemplate).toEqual('views/sidebar.html');
        expect(MainCtrl.adTemplate).toEqual('views/ads/ad-holder.html');

        player = {displayName: 'ABC', md5: '6666', adminUser: false, source: 'MANUAL'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
        expect(MainCtrl.currentPlayer).toEqual(player);
        expect(MainCtrl.currentPlayer.displayName).toEqual('ABC');
        expect(MainCtrl.playerGreeting).toEqual('Welcome ABC');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.includeTemplate).toEqual('views/sidebar.html');
        expect(MainCtrl.adTemplate).toEqual('views/ads/ad-holder.html');
        expect(versionNotesService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(
            1.3,
            'Reorganized the game lists, more games per day and fewer ads.');
    });

    it('logout', function () {
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
        expect(MainCtrl.includeTemplate).toEqual('views/sidebar.html');
        expect(MainCtrl.adTemplate).toEqual('views/ads/ad-holder.html');
        expect(logoutCalled).toEqual(false);
        MainCtrl.logout();
        expect(logoutCalled).toEqual(true);
        expect(MainCtrl.includeTemplate).toEqual('views/empty.html');
        expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
    });

});
