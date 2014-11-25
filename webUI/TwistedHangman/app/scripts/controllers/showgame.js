'use strict';

var LETTERA = 'A'.charCodeAt(0);

angular.module('twistedHangmanApp').controller('ShowCtrl', function ($scope, $routeParams, $http, twCurrentPlayerService, $window) {
  $scope.gameID = $routeParams.gameID;
  $scope.workingWordPhraseArray = [];
  $scope.workingWordPhraseClasses = [];
  $scope.letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
  $scope.letterClasses = [];
  $scope.letters.forEach(function () {
    $scope.letterClasses.push('regular');
  });

  twCurrentPlayerService.currentPlayer().then(function (data) {
    $scope.player = data;
    $scope.computeGameState();
  }, function () {
    // TODO
  });

  $http.get(twCurrentPlayerService.currentPlayerBaseURL() + 'play/' + $scope.gameID).success(function (data) {
    $scope.game = data;
    $scope.lastUpdate = new Date($scope.game.lastUpdate * 1000);
    $scope.created = new Date($scope.game.created * 1000);
    $scope.computeGameState();
    //  TODO
    console.log(data);
  }).error(function (data, status, headers, config) {
    //  TODO
    console.log(data + status + headers + config);
  });

  $scope.computeGameState = function () {
    if (typeof $scope.player === 'undefined' || typeof $scope.game === 'undefined') {
      return;
    }
    $scope.gameState = $scope.game.solverStates[$scope.player.md5];
    $scope.computeImage();
    $scope.computeWordPhrase();
    $scope.computeKeyboard();
  };

  $scope.computeWordPhrase = function () {
    $scope.workingWordPhraseArray = $scope.gameState.workingWordPhrase.split('');
    for (var i = 0; i < $scope.workingWordPhraseArray.length; i++) {
      var r = 'regular';
      if (typeof $scope.gameState.featureData.ThievingPositionTracking !== 'undefined') {
        r = ($scope.gameState.featureData.ThievingPositionTracking[i] === true) ? 'stolen' : 'notstolen';
      }
      $scope.workingWordPhraseClasses[i] = r;
    }
  };

  $scope.computeImage = function () {
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

  $scope.computeKeyboard = function () {
    $scope.gameState.guessedLetters.forEach(function (item) {
      $scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'guessed';
    });
    $scope.gameState.badlyGuessedLetters.forEach(function (item) {
      $scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'badguess';
    });
  };
});
