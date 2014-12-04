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
      expect(service.get('Phase1')).toEqual([]);
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

    it('basic test', function () {
    });

  });
});
