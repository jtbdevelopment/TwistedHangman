'use strict';

describe('Service: twAds', function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var appCB;
    var service, $q, $rootScope, adCalls;
    beforeEach(inject(function (_$q_, $injector, _$rootScope_) {
        window.invokeApplixirVideoUnitExtended = function (b, p, cb) {
            expect(b).toEqual(false);
            expect(p).toEqual('middle');
            appCB = cb;
            adCalls += 1;
        };
        adCalls = 0;
        $q = _$q_;
        $rootScope = _$rootScope_;
        appCB = undefined;
        service = $injector.get('twAds');
    }));

    it('opens add and resolves promise normally', function () {
        var promiseResolved = false;
        service.showAdPopup().then(function () {
            promiseResolved = true;
        });
        appCB();
        $rootScope.$apply();
        expect(promiseResolved).toEqual(true);
    });

    it('showing ad shortly after first time does nothing and resolves promise promise normally', function () {
        var promiseResolved = false;
        service.showAdPopup().then(function () {
            promiseResolved = true;
        });
        appCB();
        $rootScope.$apply();
        expect(promiseResolved).toEqual(true);

        promiseResolved = false;
        service.showAdPopup().then(function () {
            promiseResolved = true;
        });
        $rootScope.$apply();
        expect(promiseResolved).toEqual(true);
        expect(adCalls).toEqual(1);
    });

    it('passes on ad if exception throw', function () {
        window.invokeApplixirVideoUnitExtended = function (b, p) {
            expect(b).toEqual(false);
            expect(p).toEqual('middle');
            throw 'Error!';
        };
        var promiseResolved = false;
        service.showAdPopup().then(function () {
            promiseResolved = true;
        });
        $rootScope.$apply();
        expect(promiseResolved).toEqual(true);
    });
});
