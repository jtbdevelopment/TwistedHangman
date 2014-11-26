'use strict';

var LETTERA = 'A'.charCodeAt(0);

var sharedScope;

function computeWordPhrase() {
  sharedScope.workingWordPhraseArray = sharedScope.gameState.workingWordPhrase.split('');
  for (var i = 0; i < sharedScope.workingWordPhraseArray.length; i++) {
    var r = 'regularwp';
    if (typeof sharedScope.gameState.featureData.ThievingPositionTracking !== 'undefined') {
      r = (sharedScope.gameState.featureData.ThievingPositionTracking[i] === true) ? 'stolenwp' : 'stealablewp';
    }
    sharedScope.workingWordPhraseClasses[i] = r;
  }
}

function computeImage() {
  if (sharedScope.gameState.penalties === sharedScope.gameState.maxPenalties) {
    sharedScope.image = 'hangman13.png';
    return;
  }
  var n = 0;
  switch (sharedScope.gameState.maxPenalties) {
    case 6:
    case 10:
      n = sharedScope.gameState.penalties + 3;
      break;
    default:
      n = sharedScope.gameState.penalties;
      break;
  }
  sharedScope.image = 'hangman' + n + '.png';
}

function computeKeyboard() {
  //  TODO - show stolen letters when stealing auto gets the rest
  sharedScope.gameState.guessedLetters.forEach(function (item) {
    sharedScope.letterClasses[item.charCodeAt(0) - LETTERA] = 'guessedkb';
  });
  sharedScope.gameState.badlyGuessedLetters.forEach(function (item) {
    sharedScope.letterClasses[item.charCodeAt(0) - LETTERA] = 'badguesskb';
  });
}

function computeGameState() {
  if (typeof sharedScope.player === 'undefined' || typeof sharedScope.game === 'undefined') {
    return;
  }
  sharedScope.gameState = sharedScope.game.solverStates[sharedScope.player.md5];
  computeImage();
  computeWordPhrase();
  computeKeyboard();
}

function processGame(data) {
  sharedScope.game = data;
  sharedScope.lastUpdate = new Date(sharedScope.game.lastUpdate * 1000);
  sharedScope.created = new Date(sharedScope.game.created * 1000);
  computeGameState();
}

function processUpdate(rootScope, data) {
  var beforePhase = sharedScope.game.gamePhase;
  processGame(data);
  rootScope.$broadcast('refreshGames', data.gamePhase);
  if (data.gamePhase !== beforePhase) {
    rootScope.$broadcast('refreshGames', beforePhase);
  }
  //  TODO
  console.log(data);
}


//  TODO - testing

angular.module('twistedHangmanApp').controller('ShowCtrl', function ($rootScope, $scope, $routeParams, $http, twCurrentPlayerService) {
  sharedScope = $scope;
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
    computeGameState();
  }, function () {
    // TODO
  });

  $http.get(twCurrentPlayerService.currentPlayerBaseURL() + 'play/' + $scope.gameID).success(function (data) {
    processGame(data);
  }).error(function (data, status, headers, config) {
    //  TODO
    console.log(data + status + headers + config);
  });
});

angular.module('twistedHangmanApp').controller('PlayCtrl', function ($rootScope, $scope, $http, twCurrentPlayerService) {
  $scope.sharedScope = sharedScope;

  $scope.sendGuess = function (letter) {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + 'play/' + sharedScope.gameID + '/guess/' + letter).success(function (data) {
      processUpdate($rootScope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.log(data + status + headers + config);
    });
  };

  $scope.stealLetter = function (position) {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + 'play/' + sharedScope.gameID + '/steal/' + position).success(function (data) {
      processUpdate($rootScope, data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.log(data + status + headers + config);
    });
  };
});

angular.module('twistedHangmanApp').controller('RematchCtrl', function ($rootScope, $scope, $http, twCurrentPlayerService, $window) {
  $scope.sharedScope = sharedScope;

  $scope.startRematch = function () {
    $http.put(twCurrentPlayerService.currentPlayerBaseURL() + 'play/' + sharedScope.gameID + '/rematch').success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      $rootScope.$broadcast('refreshGames', 'Rematched');
      $rootScope.$broadcast('refreshGames', 'Rematch');
      processGame(data);
      $window.location.replace('#/show/' + data.id);
      //  TODO
      console.log(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.log(data + status + headers + config);
    });
  };
});

angular.module('twistedHangmanApp').controller('GameSummaryCtrl', function ($rootScope, $scope, $http, twCurrentPlayerService) {
  $scope.sharedScope = sharedScope;

  $scope.roleForPlayer = function (md5) {
    if (md5 === sharedScope.game.puzzleSetter) {
      return 'Set Puzzle';
    }
    return 'Solver';
  };

  $scope.gameEndForPlayer = function (md5) {
    if (md5 === sharedScope.game.puzzleSetter) {
      return 'N/A';
    }

    var solverState = sharedScope.game.solverStates[md5];
    return solverState.isGameOver ? (solverState.isGameWon ? 'Solved' : 'Hung') : 'Incomplete';
  };

  $scope.gameScoreForPlayer = function (md5) {
    //  TODO
    return 0;
  };

  $scope.runningScoreForPlayer = function (md5) {
    return sharedScope.game.playerScores[md5];
  };
});
