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

      twFacebook.canAutoSignIn(function (auto) {
        if (!auto) {
          $scope.showFacebook = true;
          $scope.showManual = true;
        } else {
          $scope.message = 'Logging in via Facebook';
          $window.location = '/auth/facebook';
        }
      });
    }]);
