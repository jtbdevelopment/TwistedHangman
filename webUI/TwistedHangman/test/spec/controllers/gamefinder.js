'use strict';

var phasesAndSymbols = {
  Playing: 'play',
  Setup: 'comment',
  Challenged: 'inbox',
  RoundOver: 'repeat',
  Declined: 'remove',
  NextRoundStarted: 'ok-sign',
  Quit: 'flag'
};
angular.forEach(phasesAndSymbols, function (glyph, phase) {
  var name = phase + 'Games';
  var test = 'Controller: ' + name + 'Games';
  var url = '/api/player/MANUAL1/games/' + phase + '?pageSize=100';

  describe(test, function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, httpBackend, scope, rootScope, playerService, phaseService, q, phaseDeferred, location;
    var phaseDesc = {};
    phaseDesc[phase] = ['unused', 'desc'];
    var status = [{'X': 1}, {'Y': 2}];

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($httpBackend, $controller, $rootScope, $q) {
      scope = $rootScope.$new();
      httpBackend = $httpBackend;
      location = {path: jasmine.createSpy()};
      rootScope = $rootScope;
      q = $q;
      playerService = {
        currentPlayerBaseURL: function () {
          return '/api/player/MANUAL1';
        }
      };
      phaseService = {
        phases: function () {
          phaseDeferred = q.defer();
          return phaseDeferred.promise;
        }
      };

      ctrl = $controller(name, {
        $scope: scope,
        $location: location,
        twCurrentPlayerService: playerService,
        twGamePhaseService: phaseService
      });
    }));

    describe('error initialization tests', function () {
      it('goes to error page if phase errors', function () {
        httpBackend.expectGET(url).respond(status);
        phaseDeferred.reject();
        rootScope.$apply();
        httpBackend.flush();
        expect(location.path).toHaveBeenCalledWith('/error');
      });

      it('goes to error page if initial load errors', function () {
        httpBackend.expectGET(url).respond(401, {});
        phaseDeferred.resolve(phaseDesc);
        rootScope.$apply();
        httpBackend.flush();
        expect(location.path).toHaveBeenCalledWith('/error');
      });
    });

    describe('normal initialization tests', function () {
      beforeEach(function () {
        httpBackend.expectGET(url).respond(status);
        phaseDeferred.resolve(phaseDesc);
        rootScope.$apply();
        httpBackend.flush();
      });

      it('sets games to empty initially and then calls http', function () {
        expect(scope.label).toEqual('desc');
        expect(scope.glyph).toEqual('glyphicon-' + glyph);
        expect(scope.style).toEqual(phase.toLowerCase() + 'Button');
        expect(scope.games).toEqual(status);
      });

      it('refreshes on "refreshGames" broadcast with no params', function () {
        expect(scope.games).toEqual(status);
        var newStatus = [{'Y': 2}];
        httpBackend.expectGET(url).respond(newStatus);
        rootScope.$broadcast('refreshGames', '');
        httpBackend.flush();
        expect(scope.games).toEqual(newStatus);
      });

      it('refreshes on "refreshGames" broadcast with  params', function () {
        expect(scope.games).toEqual(status);
        var newStatus = [{'Y': 2}, {'Z': 5}];
        httpBackend.expectGET(url).respond(newStatus);
        rootScope.$broadcast('refreshGames', phase);
        httpBackend.flush();
        expect(scope.games).toEqual(newStatus);
      });

      it('does not refreshes on "refreshGames" broadcast with non-matching param', function () {
        httpBackend.resetExpectations();
        expect(scope.games).toEqual(status);
        rootScope.$broadcast('refreshGames', 'BAD');
        expect(scope.games).toEqual(status);
        httpBackend.verifyNoOutstandingRequest();
        httpBackend.verifyNoOutstandingExpectation();
      });

      it('refreshes on "playerSwitch" broadcast', function () {
        expect(scope.games).toEqual(status);
        var newStatus = [{'Y': 2}, {'Z': 5}];
        httpBackend.expectGET(url).respond(newStatus);
        spyOn(playerService, 'currentPlayerBaseURL').and.callThrough();
        rootScope.$broadcast('playerSwitch');
        httpBackend.flush();
        expect(playerService.currentPlayerBaseURL).toHaveBeenCalled();
        expect(scope.games).toEqual(newStatus);
      });
    });
  });
});
