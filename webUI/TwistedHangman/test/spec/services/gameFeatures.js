'use strict';


//  TODO - find out how to test
describe('Service: gameFeatures', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var service, httpBackend;
  var result = {DrawGallows: 'Draw the gallows', Thieving: 'Yee-haw!'};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($httpBackend, $injector) {
    httpBackend = $httpBackend;
    httpBackend.expectGET('/api/player/features').respond(result);
    service = $injector.get('twGameFeatureService');
  }));

  it('sets games to empty initially and then calls http', function () {
    var d = null;
    service.features().then(function (data) {
      d = data;
    }, function (error) {
      d = error;
    });
    httpBackend.flush();

    expect(d).toEqual(result);
  });
});
