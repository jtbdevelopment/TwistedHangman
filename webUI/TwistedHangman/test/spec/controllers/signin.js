'use strict';

describe('Controller: SignInCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var SignInCtrl, scope, q, mockFacebook, facebookDeferred;
  var cookies = {
    somethin: 'somethin',
  };
  var window;


  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $q) {
    q = $q;
    window = {location: jasmine.createSpy()};
    cookies['XSRF-TOKEN'] = 'TOKEN';
    mockFacebook = {
      canAutoSignIn: function () {
        facebookDeferred = q.defer();
        return facebookDeferred.promise;
      }
    };
    scope = $rootScope.$new();
    SignInCtrl = $controller('SignInCtrl', {
      $scope: scope,
      $cookies: cookies,
      $window: window,
      twFacebook: mockFacebook
    });
  }));

  it('initializes', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
  });

  it('initializes and can autologin', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    facebookDeferred.resolve(true);
    scope.$apply();
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Logging in via Facebook');
    expect(window.location).toEqual('/auth/facebook');
  });

  it('initializes and cannot autologin with localhost', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    window.location = {href: 'somethinglocalhostsomething'};
    facebookDeferred.resolve(false);
    scope.$apply();
    expect(scope.showFacebook).toEqual(true);
    expect(scope.showManual).toEqual(true);
    expect(scope.message).toEqual('');
  });

  it('errors with localhost', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    window.location = {href: 'somethinglocalhostsomething'};
    facebookDeferred.reject();
    scope.$apply();
    expect(scope.showFacebook).toEqual(true);
    expect(scope.showManual).toEqual(true);
    expect(scope.message).toEqual('');
  });

  it('initializes and cannot autologin with -dev', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    window.location = {href: 'something-devsomething'};
    facebookDeferred.resolve(false);
    scope.$apply();
    expect(scope.showFacebook).toEqual(true);
    expect(scope.showManual).toEqual(true);
    expect(scope.message).toEqual('');
  });

  it('errors with -dev', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    window.location = {href: 'something-devsomething'};
    facebookDeferred.reject();
    scope.$apply();
    expect(scope.showFacebook).toEqual(true);
    expect(scope.showManual).toEqual(true);
    expect(scope.message).toEqual('');
  });

  it('initializes and cannot autologin with non-manual', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    window.location = {href: 'somethingsomething'};
    facebookDeferred.resolve(false);
    scope.$apply();
    expect(scope.showFacebook).toEqual(true);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('');
  });

  it('errors with non-manual', function () {
    expect(scope.csrf).toEqual('TOKEN');
    expect(scope.showFacebook).toEqual(false);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('Initializing...');
    window.location = {href: 'somethingsomething'};
    facebookDeferred.reject();
    scope.$apply();
    expect(scope.showFacebook).toEqual(true);
    expect(scope.showManual).toEqual(false);
    expect(scope.message).toEqual('');
  });
});
