'use strict';

describe('Controller: ConfirmCtrl', function () {

    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, scope, modalInstance;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        modalInstance = jasmine.createSpyObj('modalInstance', ['close', 'dismiss']);
        ctrl = $controller('ConfirmCtrl', {
            $scope: scope,
            $uibModalInstance: modalInstance
        });
    }));

    it('calls close on ok', function () {
        scope.confirm();
        expect(modalInstance.close).toHaveBeenCalled();
    });

    it('calls dismiss on cancel', function () {
        scope.cancel();
        expect(modalInstance.dismiss).toHaveBeenCalled();
    });
});
