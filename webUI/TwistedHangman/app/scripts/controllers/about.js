'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });