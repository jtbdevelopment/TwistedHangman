'use strict';

var phasesAndSymbols = {
  Playing: 'play',
  Setup: 'comment',
  Challenge: 'inbox',
  Rematch: 'repeat',
  Declined: 'remove',
  Rematched: 'flag'
};
angular.forEach(phasesAndSymbols, function (glyph, phase) {
  var name = phase + 'Games';
  var test = 'Controller: ' + name + 'Games';
  var url = '/api/player/MANUAL1/games/' + phase + '?pageSize=100';

  describe(test, function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, httpBackend, scope, rootScope;
    var status = [{'X': 1}, {'Y': 2}];

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($httpBackend, $controller, $rootScope) {
      scope = $rootScope.$new();
      httpBackend = $httpBackend;
      httpBackend.expectGET(url).respond(status);
      rootScope = $rootScope;
      ctrl = $controller(name, {
        $scope: scope
      });
    }));

    it('sets games to empty initially and then calls http', function () {
      expect(scope.games).toEqual([]);
      expect(scope.label).toEqual(phase);
      expect(scope.glyph).toEqual('glyphicon-' + glyph);
      expect(scope.style).toEqual(phase.toLowerCase() + 'Button');
      httpBackend.flush();
      expect(scope.games).toEqual(status);
    });

    it('refreshes on "refreshGames" broadcast with no params', function () {
      httpBackend.flush();
      expect(scope.games).toEqual(status);
      var newStatus = [{'Y': 2}];
      httpBackend.expectGET(url).respond(newStatus);
      rootScope.$broadcast('refreshGames', '');
      httpBackend.flush();
      expect(scope.games).toEqual(newStatus);
    });

    it('refreshes on "refreshGames" broadcast with  params', function () {
      httpBackend.flush();
      expect(scope.games).toEqual(status);
      var newStatus = [{'Y': 2}, {'Z': 5}];
      httpBackend.expectGET(url).respond(newStatus);
      rootScope.$broadcast('refreshGames', phase);
      httpBackend.flush();
      expect(scope.games).toEqual(newStatus);
    });

    it('does not refreshes on "refreshGames" broadcast with non-matching param', function () {
      httpBackend.flush();
      httpBackend.resetExpectations();
      expect(scope.games).toEqual(status);
      rootScope.$broadcast('refreshGames', 'BAD');
      expect(scope.games).toEqual(status);
      httpBackend.verifyNoOutstandingRequest();
      httpBackend.verifyNoOutstandingExpectation();
    });
  });
});
