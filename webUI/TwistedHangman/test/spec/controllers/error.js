'use strict';

describe('Controller: ErrorCtrl', function () {

    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, scope, modalInstance;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        modalInstance = jasmine.createSpyObj('modalInstance', ['close', 'dismiss']);
        ctrl = $controller('ErrorCtrl', {
            $scope: scope,
            $uibModalInstance: modalInstance,
            message: 'X'
        });
    }));

    it('initializes', function () {
        expect(scope.message).toEqual('X');
    });

    it('calls dismiss on cancel', function () {
        scope.ok();
        expect(modalInstance.dismiss).toHaveBeenCalled();
    });
});
