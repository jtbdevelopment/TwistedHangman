'use strict';

var SCOPE = 'scope';

angular.module('twistedHangmanApp').controller('ShowCtrl', function ($rootScope, $scope, $routeParams, $http, $location, twCurrentPlayerService, twShowGameCache, twShowGameService) {
  $scope.gameID = $routeParams.gameID;
  twShowGameService.initializeScope($scope);

  twCurrentPlayerService.currentPlayer().then(function (data) {
    $scope.player = data;
    twShowGameService.computeGameState();
  }, function () {
    // TODO
  });

  $http.get(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID).success(function (data) {
    twShowGameService.processGame(data);
  }).error(function (data, status, headers, config) {
    //  TODO
    console.error(data + status + headers + config);
  });

  $scope.startRematch = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/rematch').success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      $rootScope.$broadcast('refreshGames', 'Rematched');
      $rootScope.$broadcast('refreshGames', 'Rematch');
      twShowGameService.processGame(data);
      $location.path('/show/' + data.id);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  //  TODO - test
  $scope.accept = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/accept').success(function (data) {
      twShowGameService.processUpdate(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };
  //  TODO - test
  $scope.reject = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/reject').success(function (data) {
      twShowGameService.processUpdate(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.sendGuess = function (letter) {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/guess/' + letter).success(function (data) {
      twShowGameService.processUpdate(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  $scope.stealLetter = function (position) {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + '/play/' + $scope.gameID + '/steal/' + position).success(function (data) {
      twShowGameService.processUpdate(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };
});

angular.module('twistedHangmanApp').controller('GameSummaryCtrl', function ($scope, twShowGameCache) {
  $scope.sharedScope = twShowGameCache.get(SCOPE);

  $scope.roleForPlayer = function (md5) {
    if (md5 === $scope.sharedScope.game.puzzleSetter) {
      return 'Set Puzzle';
    }
    return 'Solver';
  };

  $scope.gameEndForPlayer = function (md5) {
    if (md5 === $scope.sharedScope.game.puzzleSetter) {
      return 'N/A';
    }

    var solverState = $scope.sharedScope.game.solverStates[md5];
    if (typeof solverState === 'undefined') {
      return 'Unknown';
    }
    return solverState.isGameOver ? (solverState.isGameWon ? 'Solved' : 'Hung') : 'Incomplete';
  };

  $scope.gameScoreForPlayer = function (md5) {
    //  TODO
    return md5.length - md5.length;
  };

  $scope.runningScoreForPlayer = function (md5) {
    return $scope.sharedScope.game.playerScores[md5];
  };
});
