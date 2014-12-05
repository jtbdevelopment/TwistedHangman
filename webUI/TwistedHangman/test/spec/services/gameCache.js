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

    /*
     it('initialize games (including override of existing value', function () {
     service.putUpdatedGame({id: 'oldid', gamePhase: 'Phase1'});
     expect(service.getAllForPhase('Phase1').info()).toEqual({id: 'game-cache-Phase1', size: 1} );
     expect(service.getAllForPhase('Phase2').info()).toEqual({id: 'game-cache-Phase2', size: 0} );
     expect(service.getAllForPhase('Phase3').info()).toEqual({id: 'game-cache-Phase3', size: 0} );
     expect(service.getAllForPhase('All').info()).toEqual({id: 'game-cache-All', size: 1} );
     //      expect(service.getAllForPhase('Phase1').get('oldid'))
    });
     */
  });
});
