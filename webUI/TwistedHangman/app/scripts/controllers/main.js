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
  ['$rootScope', '$scope', '$location', '$timeout', 'twPlayerService', 'twGameDetails',
    function ($rootScope, $scope, $location, $timeout, twPlayerService, twGameDetails) {
      $scope.playerGreeting = '';
      //  TODO - initialize from local storage?
      $scope.alerts = [];
      $scope.createRefreshEnabled = false;

      function gamePath(id) {
        return '/show/' + id;
      }

      $scope.goToGame = function (index) {
        if (index < 0 || index >= $scope.alerts.length) {
          return;
        }
        var game = $scope.alerts[index];
        $scope.alerts.splice(index, 1);
        $location.path(gamePath(game.id));
      };

      $scope.refreshGames = function () {
        $rootScope.$broadcast('refreshGames', '');
      };

      $scope.$on('playerLoaded', function () {
        $scope.alerts = [];
        $scope.playerGreeting = 'Welcome ' + twPlayerService.currentPlayer().displayName;
        $scope.createRefreshEnabled = true;
      });

      function generateAlert(gameId, alertMessage) {
        $timeout(function () {
          //  Don't show alerts if game is currently on display
          if ($location.path().indexOf(gamePath(gameId)) < 0) {
            $scope.alerts.splice(0, 0, {
              id: gameId,
              message: alertMessage
            });
          }
        }, 1);
      }

      $scope.$on('roundOverAlert', function (event, game) {
        var score = twGameDetails.gameScoreForPlayer(game, twPlayerService.currentPlayer().md5);
        if (score > 0) {
          generateAlert(game.id, 'Round ended and you scored!');
        }
        else if (score === 0) {
          generateAlert(game.id, 'Round ended and you drew.');
        } else {
          generateAlert(game.id, 'Round ended and you lost!');
        }
      });

      $scope.$on('playAlert', function (event, game) {
        if (game.features.indexOf('TurnBased') >= 0) {
          generateAlert(game.id, 'It\'s you\'re turn!');
        } else {
          generateAlert(game.id, 'Time to play!');
        }
      });

      $scope.$on('challengedAlert', function (event, game) {
        generateAlert(game.id, 'You\'ve been challenged!');
      });

      $scope.$on('declinedAlert', function (event, game) {
        generateAlert(game.id, 'A challenge was declined!');
      });

      $scope.$on('quitAlert', function (event, game) {
        generateAlert(game.id, 'A game was quit!');
      });

      $scope.$on('setupAlert', function (event, game) {
        generateAlert(game.id, 'You need to enter a puzzle!');
      });
    }]);
