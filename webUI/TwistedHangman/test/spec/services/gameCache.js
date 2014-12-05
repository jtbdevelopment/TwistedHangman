'use strict';

describe('Service: gameCache', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var phaseDeferred, q;
  var phases = {Phase1: [], Phase2: [], Phase3: []};
  beforeEach(module(function ($provide) {
    $provide.factory('twGamePhaseService', ['$q', function ($q) {
      return {
        phases: function () {
          phaseDeferred = $q.defer();
          return phaseDeferred.promise;
        }
      };
    }]);
  }));

  var service, rootScope, location;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($injector, $q, $rootScope, $location) {
    q = $q;
    rootScope = $rootScope;
    location = $location;
    spyOn(location, 'path');
    service = $injector.get('twGameCache');
  }));

  describe('test initialization', function () {
    it('initializes cache from phases', function () {
      phaseDeferred.resolve(phases);
      rootScope.$apply();
      expect(service.getAllForPhase('Phase1')).toEqual({});
      expect(service.getAllForPhase('Phase2')).toEqual({});
      expect(service.getAllForPhase('Phase3')).toEqual({});
      expect(service.getAllForPhase('All')).toEqual({});
    });

    it('errors when phases errors', function () {
      phaseDeferred.reject();
      rootScope.$apply();
      expect(location.path).toHaveBeenCalledWith('/error');
    });
  });

  describe('test usage', function () {
    beforeEach(function () {
      phaseDeferred.resolve(phases);
      rootScope.$apply();
    });

    var ng1 = {id: 'ng1', gamePhase: 'Phase1'};
    var ng2 = {id: 'ng2', gamePhase: 'Phase1'};
    var ng3 = {id: 'ng3', gamePhase: 'Phase2'};
    var ng4 = {id: 'ng4', gamePhase: 'Phase1'};

    it('initialize games (including override of existing value', function () {
      var oldgame = {id: 'oldid', gamePhase: 'Phase1'};
      service.putUpdatedGame(oldgame);
      expect(service.getAllForPhase('Phase1')).toEqual({oldid: oldgame});
      expect(service.getAllForPhase('Phase2')).toEqual({});
      expect(service.getAllForPhase('Phase3')).toEqual({});
      expect(service.getAllForPhase('All')).toEqual({oldid: oldgame});

      service.initializeGames([ng1, ng2, ng3, ng4]);
      expect(service.getAllForPhase('Phase1')).toEqual({ng1: ng1, ng2: ng2, ng4: ng4});
      expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3});
      expect(service.getAllForPhase('Phase3')).toEqual({});
      expect(service.getAllForPhase('All')).toEqual({ng1: ng1, ng2: ng2, ng3: ng3, ng4: ng4});
    });

    describe('test updates', function () {
      beforeEach(function () {
        service.initializeGames([ng1, ng2, ng3, ng4]);
      });

      it('takes in a new game update', function () {
        var ng5 = {id: 'ng5', gamePhase: 'Phase2'};
        service.putUpdatedGame(ng5);
        expect(service.getAllForPhase('Phase1')).toEqual({ng1: ng1, ng2: ng2, ng4: ng4});
        expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3, ng5: ng5});
        expect(service.getAllForPhase('Phase3')).toEqual({});
        expect(service.getAllForPhase('All')).toEqual({ng1: ng1, ng2: ng2, ng3: ng3, ng4: ng4, ng5: ng5});
      });

      it('takes in a newer game update', function () {
        ng1.lastUpdate = 1000;
        var ng1v2 = angular.copy(ng1);
        ng1v2.lastUpdate = 1001;
        service.putUpdatedGame(ng1v2);
        expect(service.getAllForPhase('Phase1')).toEqual({ng1: ng1v2, ng2: ng2, ng4: ng4});
        expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3});
        expect(service.getAllForPhase('Phase3')).toEqual({});
        expect(service.getAllForPhase('All')).toEqual({ng1: ng1v2, ng2: ng2, ng3: ng3, ng4: ng4});
      });

      it('rejects a stale game update, matching time', function () {
        ng1.lastUpdate = 1000;
        var ng1v2 = angular.copy(ng1);
        ng1v2.lastUpdate = 1000;
        ng1v2.someDifferentiator = 'X';
        service.putUpdatedGame(ng1v2);
        expect(service.getAllForPhase('Phase1')).toEqual({ng1: ng1, ng2: ng2, ng4: ng4});
        expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3});
        expect(service.getAllForPhase('Phase3')).toEqual({});
        expect(service.getAllForPhase('All')).toEqual({ng1: ng1, ng2: ng2, ng3: ng3, ng4: ng4});
      });

      it('rejects a stale game update, older time', function () {
        ng1.lastUpdate = 1000;
        var ng1v2 = angular.copy(ng1);
        ng1v2.lastUpdate = 999;
        ng1v2.someDifferentiator = 'X';
        service.putUpdatedGame(ng1v2);
        expect(service.getAllForPhase('Phase1')).toEqual({ng1: ng1, ng2: ng2, ng4: ng4});
        expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3});
        expect(service.getAllForPhase('Phase3')).toEqual({});
        expect(service.getAllForPhase('All')).toEqual({ng1: ng1, ng2: ng2, ng3: ng3, ng4: ng4});
      });
    });
  });
});
