'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
  .controller('SignInCtrl',
  ['$scope', '$window', 'twFacebook',
    function ($scope, $window, twFacebook) {
      $scope.message = 'Initializing...';
      $scope.showFacebook = false;
      $scope.showManual = false;

      function showLoginOptions() {
        $scope.showFacebook = true;
        $scope.showManual = $window.location.href.indexOf('localhost') > -1 || $window.location.href.indexOf('-dev') > -1;
        $scope.message = '';
      }

      function autoLogin() {
        $scope.showFacebook = false;
        $scope.showManual = false;
        $scope.message = 'Logging in via Facebook';
        $window.location = '/auth/facebook';
      }

      twFacebook.canAutoSignIn().then(function (auto) {
        if (!auto) {
          showLoginOptions();
        } else {
          autoLogin();
        }
      }, function () {
        showLoginOptions();
      });
    }]);
