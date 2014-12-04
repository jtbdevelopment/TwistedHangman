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
  ['$rootScope', '$scope', '$location', 'twCurrentPlayerService',
    function ($rootScope, $scope, $location, twCurrentPlayerService) {
      $scope.playerGreeting = '';
      function loadPlayer() {
        twCurrentPlayerService.currentPlayer().then(function (data) {
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
