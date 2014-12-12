'use strict';

//  TODO - should all the playing failures refresh game?
angular.module('twistedHangmanApp').controller('ShowCtrl',
  ['$scope', '$routeParams', '$http', '$location', '$modal',
    'twCurrentPlayerService', 'twShowGameService', 'twGameCache', 'twGameDetails',
    function ($scope, $routeParams, $http, $location, $modal,
              twCurrentPlayerService, twShowGameService, twGameCache, twGameDetails) {
      twShowGameService.initializeScope($scope);
      $scope.gameID = $routeParams.gameID;
      $scope.enteredCategory = '';
      $scope.enteredWordPhrase = '';
      $scope.gameDetails = twGameDetails;
      var game = twGameCache.getGameForID($scope.gameID);
      if (angular.isDefined(game)) {
        twShowGameService.updateScopeForGame($scope, game);
      }

      twCurrentPlayerService.currentPlayer().then(function (data) {
        $scope.player = data;
        twShowGameService.updateScopeForGame($scope, $scope.game);
      }, function () {
        $location.path('/error');
      });

      $scope.$on('gameCachesLoaded', function () {
        var game = twGameCache.getGameForID($scope.gameID);
        if (angular.isDefined(game)) {
          twShowGameService.updateScopeForGame($scope, twGameCache.getGameForID($scope.gameID));
        } else {
          $location.path('/');
        }
      });

      $scope.$on('gameUpdate', function (event, id, game) {
        if (angular.isDefined($scope.game) && $scope.game.id === id) {
          //  TODO - this generates a stale update warning as game cache is also listening
          twShowGameService.processGameUpdateForScope($scope, game);
        }
      });

      function showMessage(alertMessage) {
        $modal.open({
          templateUrl: 'views/gameErrorDialog.html',
          controller: 'ErrorCtrl',
          resolve: {
            message: function () {
              return alertMessage;
            }
          }
        });
      }

      function showConfirmDialog() {
        return $modal.open({
          templateUrl: 'views/confirmDialog.html',
          controller: 'ConfirmCtrl'
        });
      }

      //  TODO - state change alerts
      //  TODO - refresh game on error on action?
      //  TODO - move the broadcast

      $scope.startNextRound = function () {
        $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/rematch').success(function (data) {
          twShowGameService.updateScopeForGame($scope, data);
          $location.path('/show/' + data.id);
        }).error(function (data, status, headers, config) {
          showMessage(status + ': ' + data);
          console.error(data + status + headers + config);
        });
      };

      $scope.accept = function () {
        $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/accept').success(function (data) {
          twShowGameService.processGameUpdateForScope($scope, data);
        }).error(function (data, status, headers, config) {
          showMessage(status + ': ' + data);
          console.error(data + status + headers + config);
        });
      };

      $scope.reject = function () {
        var modal = showConfirmDialog();
        modal.result.then(function () {
          $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/reject').success(function (data) {
            twShowGameService.processGameUpdateForScope($scope, data);
          }).error(function (data, status, headers, config) {
            showMessage(status + ': ' + data);
            console.error(data + status + headers + config);
          });
        });
      };

      $scope.setPuzzle = function () {
        $http.put(
          twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/puzzle',
          {
            category: $scope.enteredCategory,
            wordPhrase: $scope.enteredWordPhrase
          }).success(function (data) {
            twShowGameService.processGameUpdateForScope($scope, data);
          }).error(function (data, status, headers, config) {
            showMessage(status + ': ' + data);
            console.error(data + status + headers + config);
          });
      };

      $scope.sendGuess = function (letter) {
        if ($scope.allowPlayMoves) {
          $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/guess/' + letter).success(function (data) {
            twShowGameService.processGameUpdateForScope($scope, data);
          }).error(function (data, status, headers, config) {
            showMessage(status + ': ' + data);
            console.error(data + status + headers + config);
          });
        } else {
          showMessage('Not currently playable.');
        }
      };

      $scope.stealLetter = function (position) {
        if ($scope.allowPlayMoves) {
          $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/steal/' + position).success(function (data) {
            twShowGameService.processGameUpdateForScope($scope, data);
          }).error(function (data, status, headers, config) {
            showMessage(status + ': ' + data);
            console.error(data + status + headers + config);
          });
        } else {
          showMessage('Not currently playable.');
        }
      };

      $scope.quit = function () {
        var modal = showConfirmDialog();
        modal.result.then(function () {
          $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/quit').success(function (data) {
            twShowGameService.processGameUpdateForScope($scope, data);
          }).error(function (data, status, headers, config) {
            showMessage(status + ': ' + data);
            console.error(data + status + headers + config);
          });
        });
      };
    }
  ])
;
