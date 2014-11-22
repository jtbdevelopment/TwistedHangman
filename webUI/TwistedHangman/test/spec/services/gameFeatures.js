'use strict';

describe('Service: gameFeatures', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var service, httpBackend, deferred;
  var result = {DrawGallows: 'Draw the gallows', Thieving: 'Yee-haw!'};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($httpBackend, $injector, $q) {
    httpBackend = $httpBackend;
    httpBackend.expectGET('/api/player/features').respond(result);
    service = $injector.get('twGameFeatureService');
    deferred = $q.defer();
  }));

  it('sets games to http results', function () {
    var features = null;
    service.features().then(function (data) {
      features = data;
    }, function (error) {
      features = error;
    });
    httpBackend.flush();

    expect(features).toEqual(result);
  });

  it('multiple calls only one http result', function () {
    var features = null;
    service.features().then(function (data) {
      features = data;
    }, function (error) {
      features = error;
    });
    httpBackend.flush();

    expect(features).toEqual(result);

    service.features().then(function (data) {
      features = data;
    }, function (error) {
      features = error;
    });

    expect(features).toEqual(result);
  });
});
