'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
  .controller('MainCtrl',
  ['$rootScope', '$scope', '$location', 'twPlayerService',
    function ($rootScope, $scope, $location, twPlayerService) {
      $scope.playerGreeting = '';

      $scope.refreshGames = function () {
        $rootScope.$broadcast('refreshGames', '');
      };

      $scope.$on('playerLoaded', function () {
        $scope.playerGreeting = 'Welcome ' + twPlayerService.currentPlayer().displayName;
      });
    }]);
