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
      function loadPlayer() {
        twPlayerService.currentPlayer().then(function (data) {
          $scope.playerGreeting = 'Welcome ' + data.displayName;
        }, function () {
          $location.path('/error');
        });
      }

      $scope.refreshGames = function () {
        $rootScope.$broadcast('refreshGames', '');
      };

      $scope.$on('playerSwitch', function () {
        console.warn('Reload Main');
        loadPlayer();
      });

      loadPlayer();
    }]);
