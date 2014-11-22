'use strict';

describe('Service: currentPlayer', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var service, httpBackend;

  //  TODO - inject this id somewhere
  var testID = 'MANUAL1';
  var result = {id: testID, md5: 'b8da6510b173e84f6cd3a2bd697d7612', disabled: false, displayName: 'Manual Player1'};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($httpBackend, $injector) {
    httpBackend = $httpBackend;
    httpBackend.expectGET('/api/player/' + testID).respond(result);
    service = $injector.get('twCurrentPlayerService');
  }));

  it('sets current id without http', function () {
    expect(service.currentID()).toEqual(testID);
    var features = null;
    service.currentPlayer().then(function (data) {
      features = data;
    }, function (error) {
      features = error;
    });
    httpBackend.flush();

    expect(features).toEqual(result);
  });

  it('sets current details with http', function () {
    expect(service.currentID()).toEqual(testID);
    var player = null;
    service.currentPlayer().then(function (data) {
      player = data;
    }, function (error) {
      player = error;
    });
    httpBackend.flush();

    expect(player).toEqual(result);
  });

  it('multiple calls only one http result', function () {
    expect(service.currentID()).toEqual(testID);
    var player = null;
    service.currentPlayer().then(function (data) {
      player = data;
    }, function (error) {
      player = error;
    });
    httpBackend.flush();

    expect(player).toEqual(result);

    service.currentPlayer().then(function (data) {
      player = data;
    }, function (error) {
      player = error;
    });

    expect(player).toEqual(result);
  });
});

