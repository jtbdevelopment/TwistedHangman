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

      //  TODO - probably wouldnt need apply if using promises
      twFacebook.canAutoSignIn(function (auto) {
        if (!auto) {
          $scope.showFacebook = true;
          $scope.showManual = true;
          $scope.message = '';
          $scope.$apply();
        } else {
          $scope.showFacebook = false;
          $scope.showManual = false;
          $scope.message = 'Logging in via Facebook';
          $scope.$apply();
          $window.location = '/auth/facebook';
        }
      });
    }]);
