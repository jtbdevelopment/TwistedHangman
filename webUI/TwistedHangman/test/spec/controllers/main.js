'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var MainCtrl, rootScope, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    rootScope = $rootScope;
    spyOn(rootScope, '$broadcast');
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('test refresh button', function () {
    scope.refreshGames();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', '');
  });

});
