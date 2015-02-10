'use strict';

describe('Controller: PopupAdCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope;

  var modalInstance = jasmine.createSpyObj('modalInstance', ['close', 'dismiss']);

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ctrl = $controller('PopupAdCtrl', {
      $scope: scope,
      $modalInstance: modalInstance
    });
  }));

  it('pressing play closes popup', function () {
    scope.play();
    expect(modalInstance.close).toHaveBeenCalled();
    expect(modalInstance.dismiss).not.toHaveBeenCalled();
  });
});

