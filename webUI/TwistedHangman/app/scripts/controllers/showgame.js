'use strict';

angular.module('twistedHangmanApp').controller('ShowCtrl', function ($scope, $routeParams, $http, twCurrentPlayerService) {
  $scope.gameID = $routeParams.gameID;
  twCurrentPlayerService.currentPlayer().then(function (data) {
    $scope.player = data;
    $scope.computeGameState();
  }, function () {
    // TODO
  });
  $scope.computeGameState = function () {
    if ($scope.player !== 'undefined' && $scope.game !== 'undefined') {
      $scope.gameState = $scope.game.solverStates[$scope.player.md5];
      $scope.computeImage();
    }
  };
  $scope.computeImage = function () {
    if ($scope.gameState.penalties === 0) {
      $scope.image = 'hangman0.png';
      return;
    }
    if ($scope.gameState.penalties === $scope.gameState.maxPenalties) {
      $scope.image = 'hangman13.png';
      return;
    }
    var n = 0;
    switch ($scope.gameState.maxPenalties) {
      case 6:
      case 10:
        n = $scope.gameState.penalties + 3;
        break;
      default:
        n = $scope.gameState.penalties;
        break;
    }
    $scope.image = 'hangman' + n + '.png';
  };
  $http.get(twCurrentPlayerService.currentPlayerBaseURL() + 'play/' + $scope.gameID).success(function (data) {
    $scope.game = data;
    $scope.computeGameState();
    console.log(data);
  }).error(function (data, status, headers, config) {
    //  TODO
    console.log(data + status + headers + config);
  });
});
