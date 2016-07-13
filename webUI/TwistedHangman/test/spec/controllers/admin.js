'use strict';

describe('Controller: AdminCtrl', function () {

    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var AdminCtrl, scope, q, mockPlayerService, http;
    var location = {path: jasmine.createSpy()};
    var overridePID, realPID;
    var playerCount = 10 + '';
    var last24 = 11 + '';
    var last7 = 24 + '';
    var last30 = 101 + '';
    var playerCreatedLast24 = 17 + '';
    var playerCreatedLast7 = 21 + '';
    var playerCreatedLast30 = 33 + '';
    var playerLastLogin24 = 111 + '';
    var playerLastLogin7 = 22 + '';
    var playerLastLogin30 = 55 + '';

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope, $q, $httpBackend) {
        q = $q;
        http = $httpBackend;
        realPID = 'REAL';
        overridePID = '';

        mockPlayerService = {
            realPID: function () {
                return realPID;
            },
            overridePID: function (pid) {
                overridePID = pid;
            }
        };
        scope = $rootScope.$new();
        AdminCtrl = $controller('AdminCtrl', {
            $scope: scope,
            $location: location,
            $window: window,
            jtbPlayerService: mockPlayerService
        });
    }));

    it('initializes', function () {
        expect(scope.searchText).toEqual('');
        expect(scope.players).toEqual([]);
        expect(scope.selected).toEqual({});
        expect(overridePID).toEqual('');
    });

    beforeEach(function () {
        var time = Math.floor((new Date()).getTime() / 1000);
        var dayInSeconds = 86400;
        var base = '\/api\/player\/admin\/gamesSince\/';
        var time24, time7, time30;

        http.expectGET('/api/player/admin/playerCount').respond(playerCount);

        base = '\/api\/player\/admin\/playersCreated\/';
        time24 = new RegExp(base + (time - (dayInSeconds) + '').slice(0, -1) + '[0-9]');
        time7 = new RegExp(base + (time - (dayInSeconds * 7) + '').slice(0, -1) + '[0-9]');
        time30 = new RegExp(base + (time - (dayInSeconds * 30) + '').slice(0, -1) + '[0-9]');
        http.expectGET(time24).respond(playerCreatedLast24);
        http.expectGET(time7).respond(playerCreatedLast7);
        http.expectGET(time30).respond(playerCreatedLast30);

        base = '\/api\/player\/admin\/playersLoggedIn\/';
        time24 = new RegExp(base + (time - (dayInSeconds) + '').slice(0, -1) + '[0-9]');
        time7 = new RegExp(base + (time - (dayInSeconds * 7) + '').slice(0, -1) + '[0-9]');
        time30 = new RegExp(base + (time - (dayInSeconds * 30) + '').slice(0, -1) + '[0-9]');
        http.expectGET(time24).respond(playerLastLogin24);
        http.expectGET(time7).respond(playerLastLogin7);
        http.expectGET(time30).respond(playerLastLogin30);

        base = '\/api\/player\/admin\/gamesSince\/';
        time24 = new RegExp(base + (time - (dayInSeconds) + '').slice(0, -1) + '[0-9]');
        time7 = new RegExp(base + (time - (dayInSeconds * 7) + '').slice(0, -1) + '[0-9]');
        time30 = new RegExp(base + (time - (dayInSeconds * 30) + '').slice(0, -1) + '[0-9]');
        http.expectGET(time24).respond(last24);
        http.expectGET(time7).respond(last7);
        http.expectGET(time30).respond(last30);
    });

    describe('responds with possible players to imitate', function () {
        var players = [{displayName: 'X'}, {displayName: 'Y'}];
        beforeEach(function () {
            http.expectGET('/api/player/admin').respond(players);
        });

        it('initializes', function () {
            expect(scope.playerCount).toEqual(0);
            expect(scope.playersCreated24hours).toEqual(0);
            expect(scope.playersCreated7days).toEqual(0);
            expect(scope.playersCreated30days).toEqual(0);
            expect(scope.playersLastLogin24hours).toEqual(0);
            expect(scope.playersLastLogin7days).toEqual(0);
            expect(scope.playersLastLogin30days).toEqual(0);
            expect(scope.last24hours).toEqual(0);
            expect(scope.last7days).toEqual(0);
            expect(scope.last30days).toEqual(0);

            http.flush();
            expect(scope.searchText).toEqual('');
            expect(scope.players).toEqual(players);
            expect(scope.selected).toEqual({});
            expect(overridePID).toEqual('');
            expect(scope.playerCount).toEqual(playerCount);
            expect(scope.last24hours).toEqual(last24);
            expect(scope.last7days).toEqual(last7);
            expect(scope.last30days).toEqual(last30);
            expect(scope.playersCreated24hours).toEqual(playerCreatedLast24);
            expect(scope.playersCreated7days).toEqual(playerCreatedLast7);
            expect(scope.playersCreated30days).toEqual(playerCreatedLast30);
            expect(scope.playersLastLogin24hours).toEqual(playerLastLogin24);
            expect(scope.playersLastLogin7days).toEqual(playerLastLogin7);
            expect(scope.playersLastLogin30days).toEqual(playerLastLogin30);
        });

        describe('and switches', function () {
            beforeEach(function () {
                http.flush();
            });

            it('changing user', function () {
                scope.selected.id = 'X123';
                scope.changeUser();
                expect(overridePID).toEqual('X123');
                expect(location.path).toHaveBeenCalledWith('/');
            });

            it('reverting user', function () {
                scope.selected.id = 'X123';
                scope.revertUser();
                expect(overridePID).toEqual(realPID);
                expect(location.path).toHaveBeenCalledWith('/');
            });
        });
    });

    describe('responds with error on possible players to imitate', function () {
        beforeEach(function () {
            http.expectGET('/api/player/admin').respond(404, {});
        });
        it('initializes', function () {
            http.flush();
            expect(scope.searchText).toEqual('');
            expect(location.path).toHaveBeenCalledWith('/error');
            expect(scope.players).toEqual([]);
            expect(scope.selected).toEqual({});
            expect(overridePID).toEqual('');
        });
    });
});

