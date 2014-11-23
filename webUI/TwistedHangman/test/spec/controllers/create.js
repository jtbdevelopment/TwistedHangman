'use strict';

describe('Controller: SinglePlayerCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ctrl = $controller('SinglePlayerCtrl', {
      $scope: scope
    });
  }));

  it('initializes', function () {
  });
});

describe('Controller: TwoPlayerCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ctrl = $controller('TwoPlayerCtrl', {
      $scope: scope
    });
  }));

  it('initializes', function () {
  });
});


describe('Controller: MultiPlayerCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ctrl = $controller('MultiPlayerCtrl', {
      $scope: scope
    });
  }));

  it('initializes', function () {
  });
});
