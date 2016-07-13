describe('Service: twAds', function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var service, modalOpened = false, deferred, q;
    var modal = {
        open: function (params) {
            expect(params.controller).toEqual('PopupAdCtrl');
            expect(params.templateUrl).toEqual('views/popupAdDialog.html');
            expect(params.size).toEqual('lg');
            modalOpened = true;
            deferred = q.defer();
            return deferred.promise;
        }
    };
    beforeEach(module(function ($provide) {
        $provide.factory('$uibModal', function () {
            return modal;
        });
    }));
    beforeEach(inject(function ($q, $injector) {
        q = $q;
        service = $injector.get('twAds');
    }));

    /*
     it('test open', function () {
     expect(modalOpened).toEqual(false);
     var t = service.showAdPopup();
     expect(modalOpened).toEqual(true);
     expect(t).toEqual(deferred.promise);
     });
     */
});
