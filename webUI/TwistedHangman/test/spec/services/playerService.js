'use strict';

describe('Service: playerService', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var service, httpBackend, injector, rootScope, location;

  //  TODO - inject this id somewhere
  var testID = 'MANUAL1';
  var playerResult = {
    id: testID,
    md5: 'b8da6510b173e84f6cd3a2bd697d7612',
    disabled: false,
    displayName: 'Manual Player1'
  };
  var friendResult = {1: '2', 5: '6'};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($injector, $rootScope, $location, $httpBackend) {
    rootScope = $rootScope;
    location = $location;
    spyOn(rootScope, '$broadcast').and.callThrough();
    spyOn(location, 'path');
    httpBackend = $httpBackend;
    injector = $injector;
  }));

  describe('with player responses', function () {
    beforeEach(function () {
      httpBackend.expectGET('/api/player/' + testID).respond(playerResult);
      service = injector.get('twPlayerService');
    });

    it('initializes', function () {
      expect(service.currentID()).toEqual(testID);
      expect(service.currentPlayerBaseURL()).toEqual('/api/player/' + testID);
      expect(service.realPID()).toEqual(testID);
      expect(service.currentPlayer()).toBeUndefined();
      expect(service.realPlayer()).toBeUndefined();
      expect(service.realPlayerBaseURL()).toEqual('/api/player/' + testID);
      httpBackend.flush();
      expect(service.currentPlayer()).toEqual(playerResult);
      expect(service.realPlayer()).toEqual(playerResult);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('playerLoaded');
      expect(rootScope.$broadcast).toHaveBeenCalledWith('realPlayerLoaded');
      expect(location.path).not.toHaveBeenCalledWith('/error');
    });

    it('reloads on switch', function () {
      expect(service.currentID()).toEqual(testID);
      expect(service.currentPlayerBaseURL()).toEqual('/api/player/' + testID);
      expect(service.currentPlayer()).toBeUndefined();
      expect(service.realPID()).toEqual(testID);
      expect(service.realPlayer()).toBeUndefined();
      expect(service.realPlayerBaseURL()).toEqual('/api/player/' + testID);
      httpBackend.flush();
      expect(service.currentPlayer()).toEqual(playerResult);
      expect(service.realPlayer()).toEqual(playerResult);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('playerLoaded');
      expect(rootScope.$broadcast).toHaveBeenCalledWith('realPlayerLoaded');
      expect(location.path).not.toHaveBeenCalledWith('/error');

      var newPlayer = angular.copy(playerResult);

      newPlayer.id = 'NEW';
      httpBackend.expectGET('/api/player/' + newPlayer.id).respond(newPlayer);
      service.overridePID(newPlayer.id);
      expect(service.currentID()).toEqual(newPlayer.id);
      expect(service.currentPlayerBaseURL()).toEqual('/api/player/' + newPlayer.id);
      expect(service.currentPlayer()).toEqual(playerResult);
      expect(service.realPlayerBaseURL()).toEqual('/api/player/' + testID);
      httpBackend.flush();
      expect(service.currentPlayer()).toEqual(newPlayer);
      expect(rootScope.$broadcast).toHaveBeenCalledWith('playerLoaded');
      expect(location.path).not.toHaveBeenCalledWith('/error');
    });

    it('sets current friends with http', function () {
      httpBackend.expectGET('/api/player/' + testID + '/friends').respond(friendResult);
      var friends = null;
      service.currentPlayerFriends().then(function (data) {
        friends = data;
      }, function (error) {
        friends = error;
      });
      httpBackend.flush();

      expect(friends).toEqual(friendResult);
    });

    it('sets current friends with error', function () {
      httpBackend.expectGET('/api/player/' + testID + '/friends').respond(500, {err: 'error'});
      var friends;
      var errorCalled = false;
      service.currentPlayerFriends().then(function (data) {
        friends = data;
      }, function (error) {
        expect(error).toBeDefined();
        errorCalled = true;
      });
      httpBackend.flush();

      expect(errorCalled).toEqual(true);
      expect(friends).toBeUndefined();
    });

    it('multiple calls only one http friends', function () {
      httpBackend.expectGET('/api/player/' + testID + '/friends').respond(friendResult);
      expect(service.currentID()).toEqual(testID);
      var friends = null;
      service.currentPlayerFriends().then(function (data) {
        friends = data;
      }, function (error) {
        friends = error;
      });
      httpBackend.flush();

      expect(friends).toEqual(friendResult);

      service.currentPlayerFriends().then(function (data) {
        friends = data;
      }, function (error) {
        friends = error;
      });

      expect(friends).toEqual(friendResult);
    });
  });

  describe('with bad responses', function () {
    beforeEach(function () {
      httpBackend.expectGET('/api/player/' + testID).respond(500, {somethin: 'somethin'});
      service = injector.get('twPlayerService');
    });


    it('player with bad response', function () {
      expect(service.currentID()).toEqual(testID);
      expect(service.realPID()).toEqual(testID);
      expect(service.currentPlayer()).toBeUndefined();
      expect(service.realPlayer()).toBeUndefined();
      httpBackend.flush();
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('playerLoaded');
      expect(rootScope.$broadcast).not.toHaveBeenCalledWith('realPlayerLoaded');
      expect(location.path).toHaveBeenCalledWith('/error');
    });
  });

});
