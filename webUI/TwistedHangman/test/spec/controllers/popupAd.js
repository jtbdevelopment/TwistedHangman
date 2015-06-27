'use strict';

describe('Controller: PopupAdCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, timeout;

  var modalInstance = jasmine.createSpyObj('modalInstance', ['close', 'dismiss']);

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$timeout_) {
    scope = $rootScope.$new();
    timeout = _$timeout_;
    ctrl = $controller('PopupAdCtrl', {
      $scope: scope,
      $modalInstance: modalInstance
    });
  }));

  it('initializes', function () {
    expect(scope.message).toEqual('');
    expect(scope.showContinue).toEqual(false);
  });

  it('shows message for 5 seconds', function () {
    timeout.flush();
    expect(scope.message).toEqual('Continue in 5 seconds...');
    expect(scope.showContinue).toEqual(false);
    timeout.flush();
    expect(scope.message).toEqual('Continue in 4 seconds...');
    expect(scope.showContinue).toEqual(false);
    timeout.flush();
    expect(scope.message).toEqual('Continue in 3 seconds...');
    expect(scope.showContinue).toEqual(false);
    timeout.flush();
    expect(scope.message).toEqual('Continue in 2 seconds...');
    expect(scope.showContinue).toEqual(false);
    timeout.flush();
    expect(scope.message).toEqual('Continue in 1 seconds...');
    expect(scope.showContinue).toEqual(false);
    timeout.flush();
    expect(scope.message).toEqual('');
    expect(scope.showContinue).toEqual(true);
  });

  it('pressing play closes popup', function () {
    scope.play();
    expect(modalInstance.close).toHaveBeenCalled();
    expect(modalInstance.dismiss).not.toHaveBeenCalled();
  });
});

