'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
  .controller('MainCtrl', function ($rootScope, $scope, twCurrentPlayerService) {
    $scope.playerGreeting = '';
    function loadPlayer() {
      twCurrentPlayerService.currentPlayer().then(function (data) {
        $scope.playerGreeting = 'Welcome ' + data.displayName;
      }, function () {
        //  TODO
      });
    }

    loadPlayer();

    $scope.$on('playerSwitch', function () {
      console.error('Reload Main');
      loadPlayer();
    });

    $scope.refreshGames = function () {
      $rootScope.$broadcast('refreshGames', '');
    };
  });
