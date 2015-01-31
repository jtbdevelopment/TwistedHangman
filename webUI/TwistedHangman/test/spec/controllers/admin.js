'use strict';

describe('Controller: AdminCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var AdminCtrl, scope, q, mockPlayerService, http;
  var location = {path: jasmine.createSpy()};
  var overridePID, realPID;

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
      twPlayerService: mockPlayerService
    });
  }));

  it('initializes', function () {
    expect(scope.searchText).toEqual('');
    expect(scope.players).toEqual([]);
    expect(scope.selected).toEqual({});
    expect(overridePID).toEqual('');
  });

  describe('responds with possible players to imitate', function () {
    var players = [{displayName: 'X'}, {displayName: 'Y'}];
    beforeEach(function () {
      http.expectGET('/api/player/admin').respond(players);
    });
    it('initializes', function () {
      http.flush();
      expect(scope.searchText).toEqual('');
      expect(scope.players).toEqual(players);
      expect(scope.selected).toEqual({});
      expect(overridePID).toEqual('');
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

  describe('responds with possible players to imitate', function () {
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
