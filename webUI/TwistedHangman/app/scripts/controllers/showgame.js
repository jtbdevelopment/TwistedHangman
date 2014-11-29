'use strict';

angular.module('twistedHangmanApp').controller('ShowCtrl', function ($rootScope, $scope, $routeParams, $http, $location, twCurrentPlayerService, twShowGameService) {
  twShowGameService.initializeScope($scope);
  $scope.gameID = $routeParams.gameID;
  $scope.enteredCategory = '';
  $scope.enteredWordPhrase = '';

  twCurrentPlayerService.currentPlayer().then(function (data) {
    $scope.player = data;
    twShowGameService.computeGameState($scope);
  }, function () {
    // TODO
  });

  $http.get(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID).success(function (data) {
    twShowGameService.processGame($scope, data);
  }).error(function (data, status, headers, config) {
    //  TODO
    console.error(data + status + headers + config);
  });

  $scope.startRematch = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/rematch').success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      $rootScope.$broadcast('refreshGames', 'Rematched');
      $rootScope.$broadcast('refreshGames', 'Rematch');
      twShowGameService.processGame($scope, data);
      $location.path('/show/' + data.id);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.accept = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/accept').success(function (data) {
      twShowGameService.processUpdate($scope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.reject = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/reject').success(function (data) {
      twShowGameService.processUpdate($scope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.quit = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/quit').success(function (data) {
      twShowGameService.processUpdate($scope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };


  $scope.setPuzzle = function () {
    $http.put(
      twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/puzzle',
      {
        category: $scope.enteredCategory,
        wordPhrase: $scope.enteredWordPhrase
      }).success(function (data) {
        twShowGameService.processUpdate($scope, data);
      }).error(function (data, status, headers, config) {
        //  TODO
        console.error(data + status + headers + config);
      });
  };

  $scope.sendGuess = function (letter) {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/guess/' + letter).success(function (data) {
      twShowGameService.processUpdate($scope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.stealLetter = function (position) {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/steal/' + position).success(function (data) {
      twShowGameService.processUpdate($scope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.roleForPlayer = function (md5) {
    if (md5 === $scope.game.puzzleSetter) {
      return 'Set Puzzle';
    }
    return 'Solver';
  };

  $scope.gameEndForPlayer = function (md5) {
    if (md5 === $scope.game.puzzleSetter) {
      return 'N/A';
    }

    var solverState = $scope.game.solverStates[md5];
    if (typeof solverState === 'undefined') {
      return 'Unknown';
    }
    //  TODO - take into account different states before this...

    return solverState.isGameOver ? (solverState.isGameWon ? 'Solved' : 'Hung') : 'Incomplete';
  };

  $scope.gameScoreForPlayer = function (md5) {
    //  TODO
    return md5.length - md5.length;
  };

  $scope.runningScoreForPlayer = function (md5) {
    return $scope.game.playerScores[md5];
  };
});
