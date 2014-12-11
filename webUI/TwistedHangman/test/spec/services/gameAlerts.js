'use strict';

describe('Service: gameAlerts', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var rootscope, service;
  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $injector) {
    rootscope = $rootScope;
    spyOn($rootScope, '$broadcast');
    service = $injector.get('twGameAlerts');
  }));

  describe('new game entry alerts', function () {
    it('publishes new game alert', function () {
      var newgame = {id: 'x', stuff: 'here'};
      service.checkNewEntryForAlerts(newgame);
      expect(rootscope.$broadcast).toHaveBeenCalledWith('newGameEntry', 'x', newgame);
    })
  });

  describe('game update alerts', function () {

    var oldgame, newgame;
    beforeEach(function () {
      oldgame = {
        id: 'x',
        gamePhase: 'Setup'
      };

      newgame = angular.copy(oldgame);
    });

    it('publishes alert on phase change', function () {
      newgame.gamePhase = 'SetupX';
      service.checkUpdateForAlerts(oldgame, newgame);
      expect(rootscope.$broadcast).toHaveBeenCalledWith('phaseChange', newgame.id, newgame);
    });

    it('does not publish alert on non-phase change', function () {
      service.checkUpdateForAlerts(oldgame, newgame);
      expect(rootscope.$broadcast).not.toHaveBeenCalled();
    });
  })
});
