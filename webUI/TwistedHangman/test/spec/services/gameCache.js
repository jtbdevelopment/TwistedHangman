'use strict';

describe('Service: gameCache', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ng1 = {id: 'ng1', gamePhase: 'Phase1'};
  var ng2 = {id: 'ng2', gamePhase: 'Phase1'};
  var ng3 = {id: 'ng3', gamePhase: 'Phase2'};
  var ng4 = {id: 'ng4', gamePhase: 'Phase1'};

  var phaseDeferred, q;
  var phases = {Phase1: [], Phase2: [], Phase3: []};

  var baseURL = '/api/player/MANUAL1';
  var gamesURL = '/games';

  beforeEach(module(function ($provide) {
    baseURL = '/api/player/MANUAL1';
    $provide.factory('twGamePhaseService', ['$q', function ($q) {
      return {
        phases: function () {
          phaseDeferred = $q.defer();
          return phaseDeferred.promise;
        }
      };
    }]);
    $provide.factory('twCurrentPlayerService', function () {
      return {
        currentPlayerBaseURL: function () {
          return baseURL;
        }
      };
    });
  }));

  var service, rootScope, location, http;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($injector, $q, $rootScope, $location, $httpBackend) {
    q = $q;
    rootScope = $rootScope;
    location = $location;
    http = $httpBackend;
    spyOn(location, 'path');
    spyOn(rootScope, '$broadcast').and.callThrough();
    service = $injector.get('twGameCache');
  }));

  describe('test initialization', function () {
    it('initializes cache from phases and games', function () {
      http.expectGET(baseURL + gamesURL).respond([ng3]);
      phaseDeferred.resolve(phases);
      rootScope.$apply();
      http.flush();
      expect(service.getAllForPhase('Phase1')).toEqual({});
      expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3});
      expect(service.getAllForPhase('Phase3')).toEqual({});
      expect(service.getAllForPhase('All')).toEqual({ng3: ng3});
      expect(rootScope.$broadcast).toHaveBeenCalledWith('gameCachesLoaded', 1);
    });

    it('errors when phases errors', function () {
      phaseDeferred.reject();
      rootScope.$apply();
      expect(location.path).toHaveBeenCalledWith('/error');
    });

    it('errors when http.get errors', function () {
      http.expectGET(baseURL + gamesURL).respond(500, {});
      phaseDeferred.resolve(phases);
      rootScope.$apply();
      http.flush();
      expect(location.path).toHaveBeenCalledWith('/error');
    });

    it('reinitializes on player switch', function () {
      http.expectGET(baseURL + gamesURL).respond([ng3]);
      phaseDeferred.resolve(phases);
      rootScope.$apply();
      http.flush();
      expect(service.getAllForPhase('Phase1')).toEqual({});
      expect(service.getAllForPhase('Phase2')).toEqual({ng3: ng3});
      expect(service.getAllForPhase('Phase3')).toEqual({});
      expect(service.getAllForPhase('All')).toEqual({ng3: ng3});
      expect(rootScope.$broadcast).toHaveBeenCalledWith('gameCachesLoaded', 1);

      var referenceHoldover = service.getAllForPhase('All');

      baseURL = '/api/player/MANUAL3';
      http.expectGET(baseURL + gamesURL).respond([ng1]);
      rootScope.$broadcast('playerSwitch');
      http.flush();
      expect(service.getAllForPhase('Phase1')).toEqual({ng1: ng1});
      expect(service.getAllForPhase('Phase2')).toEqual({});
      expect(service.getAllForPhase('Phase3')).toEqual({});
      expect(service.getAllForPhase('All')).toEqual({ng1: ng1});
      expect(referenceHoldover).toEqual({ng1: ng1});
      expect(rootScope.$broadcast).toHaveBeenCalledWith('gameCachesLoaded', 2);
    });
  });

  describe('test usage', function () {
    beforeEach(function () {
      phaseDeferred.resolve(phases);
      http.expectGET(baseURL + gamesURL).respond([ng1, ng2, ng3, ng4]);
      rootScope.$apply();
      http.flush();
    });

    afterEach(function () {
      expect(rootScope.$broadcast).toHaveBeenCalledWith('gameCachesLoaded', 1);
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
