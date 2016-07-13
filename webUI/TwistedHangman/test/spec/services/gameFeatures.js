'use strict';

describe('Service: gameFeatures', function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var service, httpBackend;
    var result = {DrawGallows: 'Draw the gallows', Thieving: 'Yee-haw!'};

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($httpBackend, $injector) {
        httpBackend = $httpBackend;
        service = $injector.get('twGameFeatureService');
    }));

    it('sets games to http results', function () {
        var features = null;
        httpBackend.expectGET('/api/features').respond(result);
        service.features().then(function (data) {
            features = data;
        }, function (error) {
            features = error;
        });
        httpBackend.flush();

        expect(features).toEqual(result);
    });

    it('sets games to error results', function () {
        var features;
        httpBackend.expectGET('/api/features').respond(500);
        var errorCalled = false;
        service.features().then(function (data) {
            features = data;
        }, function (error) {
            expect(error).toBeDefined();
            errorCalled = true;
        });
        httpBackend.flush();

        expect(errorCalled).toEqual(true);
        expect(features).toBeUndefined();
    });

    it('multiple calls only one http result', function () {
        var features = null;
        httpBackend.expectGET('/api/features').respond(result);
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
