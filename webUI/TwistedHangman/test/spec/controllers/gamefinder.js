'use strict';

var phases = ['Playing', 'Setup', 'Challenge', 'Rematch', 'Declined', 'Rematched'];

phases.map(function (phase) {
  var name = phase + 'Games';
  var test = 'Controller: ' + name + 'Games';
  var url = '/api/player/54581119a962efc67353a45c/games/' + phase + '?pageSize=100';

  describe(test, function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, httpBackend, scope;
    var status = [{'X': 1}, {'Y': 2}];

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($httpBackend, $controller, $rootScope) {
      scope = $rootScope.$new();
      httpBackend = $httpBackend;
      httpBackend.expectGET(url).respond(status);
      ctrl = $controller(name, {
        $scope: scope
      });
    }));

    it('sets games to empty initially and then calls http', function () {
      expect(scope.games).toEqual([]);
      httpBackend.flush();
      expect(scope.games).toEqual(status);
    });
  });
});
