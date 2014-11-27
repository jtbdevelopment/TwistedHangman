'use strict';

describe('Service: currentPlayer', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var service, httpBackend;

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
  beforeEach(inject(function ($injector) {
    httpBackend = $injector.get('$httpBackend');
    service = $injector.get('twCurrentPlayerService');
  }));

  it('sets current id without http', function () {
    httpBackend.expectGET('/api/player/' + testID).respond(playerResult);
    expect(service.currentID()).toEqual(testID);
    var features = null;
    service.currentPlayer().then(function (data) {
      features = data;
    }, function (error) {
      features = error;
    });
    httpBackend.flush();

    expect(features).toEqual(playerResult);
  });

  it('sets current details with http', function () {
    httpBackend.expectGET('/api/player/' + testID).respond(playerResult);
    expect(service.currentID()).toEqual(testID);
    var player = null;
    service.currentPlayer().then(function (data) {
      player = data;
    }, function (error) {
      player = error;
    });
    httpBackend.flush();

    expect(player).toEqual(playerResult);
  });

  it('multiple calls only one http playerResult', function () {
    httpBackend.expectGET('/api/player/' + testID).respond(playerResult);
    expect(service.currentID()).toEqual(testID);
    var player = null;
    service.currentPlayer().then(function (data) {
      player = data;
    }, function (error) {
      player = error;
    });
    httpBackend.flush();

    expect(player).toEqual(playerResult);

    service.currentPlayer().then(function (data) {
      player = data;
    }, function (error) {
      player = error;
    });

    expect(player).toEqual(playerResult);
  });

  it('sets current friends with http', function () {
    httpBackend.expectGET('/api/player/' + testID + '/friends').respond(friendResult)
    expect(service.currentID()).toEqual(testID);
    var friends = null;
    service.friends().then(function (data) {
      friends = data;
    }, function (error) {
      friends = error;
    });
    httpBackend.flush();

    expect(friends).toEqual(friendResult);
  });

  it('multiple calls only one http friends', function () {
    httpBackend.expectGET('/api/player/' + testID + '/friends').respond(friendResult)
    expect(service.currentID()).toEqual(testID);
    var friends = null;
    service.friends().then(function (data) {
      friends = data;
    }, function (error) {
      friends = error;
    });
    httpBackend.flush();

    expect(friends).toEqual(friendResult);

    service.friends().then(function (data) {
      friends = data;
    }, function (error) {
      friends = error;
    });

    expect(friends).toEqual(friendResult);
  });
});

