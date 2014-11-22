'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
  .controller('MainCtrl', function ($scope, twCurrentPlayerService) {
    $scope.playerGreeting = 'Welcome ';
    twCurrentPlayerService.currentPlayer().then(function (data) {
      $scope.playerGreeting = $scope.playerGreeting + data.displayName;
    });
  });
