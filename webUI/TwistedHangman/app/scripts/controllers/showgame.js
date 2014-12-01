'use strict';

angular.module('twistedHangmanApp').controller('ShowCtrl', function ($rootScope, $scope, $routeParams, $http, $location, twCurrentPlayerService, twShowGameService) {
  twShowGameService.initializeScope($scope);
  $scope.gameID = $routeParams.gameID;
  $scope.enteredCategory = '';
  $scope.enteredWordPhrase = '';

  //  TODO - test and use and style
  $scope.alertType = 'alert-info';
  $scope.alertMessage = 'Info goes here.';

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

  $scope.startNextRound = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/rematch').success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      $rootScope.$broadcast('refreshGames', 'NextRoundStarted');
      $rootScope.$broadcast('refreshGames', 'RoundOver');
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
    if ($scope.allowPlayMoves) {
      $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/guess/' + letter).success(function (data) {
        twShowGameService.processUpdate($scope, data);
      }).error(function (data, status, headers, config) {
        //  TODO
        console.error(data + status + headers + config);
      });
    } else {
      $scope.alertType = 'alert-warning';
      $scope.alertMessage = 'Not currently playable.';
    }
  };

  $scope.stealLetter = function (position) {
    if ($scope.allowPlayMoves) {
      $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/steal/' + position).success(function (data) {
        twShowGameService.processUpdate($scope, data);
      }).error(function (data, status, headers, config) {
        //  TODO
        console.error(data + status + headers + config);
      });
    } else {
      $scope.alertType = 'alert-warning';
      $scope.alertMessage = 'Not currently playable.';
    }
  };

  $scope.roleForPlayer = function (md5) {
    if (angular.isUndefined($scope.game)) {
      //  TODO - test
      return '';
    }
    if (md5 === $scope.game.puzzleSetter) {
      return 'Set Puzzle';
    }
    return 'Solver';
  };

  $scope.gameEndForPlayer = function (md5) {
    if (angular.isUndefined($scope.game)) {
      //  TODO - test
      return '';
    }
    if (md5 === $scope.game.puzzleSetter) {
      return 'N/A';
    }

    var solverState = $scope.game.solverStates[md5];
    if (angular.isUndefined(solverState)) {
      return 'Unknown';
    }
    //  TODO - take into account different states before this...
    return solverState.isPuzzleOver ? (solverState.isPuzzleSolved ? 'Solved!' : 'Hung!') : 'Not Solved.';
  };

  //  TODO - Test
  $scope.stateForPlayer = function (md5, field) {
    if (angular.isUndefined($scope.game)) {
      return '';
    }
    if (md5 === $scope.game.puzzleSetter) {
      return 'N/A';
    }

    var solverState = $scope.game.solverStates[md5];
    if (angular.isUndefined(solverState)) {
      return 'Unknown';
    }

    return solverState[field];
  };

  $scope.gameScoreForPlayer = function (md5) {
    if (angular.isUndefined($scope.game)) {
      //  TODO - test
      return '';
    }
    return $scope.game.playerRoundScores[md5];
  };

  $scope.runningScoreForPlayer = function (md5) {
    if (angular.isUndefined($scope.game)) {
      //  TODO - test
      return '';
    }
    return $scope.game.playerRunningScores[md5];
  };
});
