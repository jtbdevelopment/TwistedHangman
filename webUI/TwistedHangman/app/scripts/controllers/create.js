'use strict';

var SINGLE_PLAYER = 'SinglePlayer';
var TWO_PLAYERS = 'TwoPlayer';
var MULTI_PLAYER = 'ThreePlus';
var SYSTEM_PUZZLES = 'SystemPuzzles';

angular.module('twistedHangmanApp').controller('CreateCtrl', function ($rootScope, $scope, twGameFeatureService, twCurrentPlayerService, $http) {
  $scope.url = twCurrentPlayerService.currentPlayerBaseURL() + 'new';
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  }, function () {
    //  TODO
  });
  $scope.thieving = 'Thieving';
  $scope.drawGallows = '';
  $scope.drawFace = '';
  $scope.gamePace = '';
  $scope.submitEnabled = false;
  $scope.players = [];
  $scope.playerCount = '';

  $scope.calcSubmitEnabled = function () {
    switch ($scope.playerCount) {
      case    SINGLE_PLAYER:
        $scope.submitEnabled = ($scope.players.length === 0);
        break;
      case    TWO_PLAYERS:
        $scope.submitEnabled = ($scope.players.length === 1);
        break;
      case    MULTI_PLAYER:
        $scope.submitEnabled = ($scope.players.length > 1);
        break;
      default:
        $scope.submitEnabled = false;
        break;
    }
  };

  $scope.setSinglePlayer = function () {
    $scope.playerCount = SINGLE_PLAYER;
    $scope.players = [];
    $scope.gamePace = '';
    $scope.puzzleSetter = SYSTEM_PUZZLES;
    $scope.winners = 'SingleWinner';
    $scope.h2hEnabled = false;
    $scope.alternatingEnabled = false;
    $scope.allFinishedEnabled = false;
    $scope.turnBasedEnabled = false;
    $scope.calcSubmitEnabled();
  };

  $scope.setTwoPlayers = function () {
    $scope.playerCount = TWO_PLAYERS;
    if ($scope.players.length > 1) {
      $scope.players = [];
    }
    $scope.h2hEnabled = true;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
    $scope.calcSubmitEnabled();
  };

  $scope.setThreePlayers = function () {
    $scope.playerCount = MULTI_PLAYER;
    if ($scope.puzzleSetter === '') {
      $scope.puzzleSetter = SYSTEM_PUZZLES;
    }
    $scope.h2hEnabled = false;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
    $scope.calcSubmitEnabled();
  };

  $scope.createGame = function () {
    var featureNames = ['puzzleSetter', 'playerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace', 'winners'];
    var featureSet = [];
    //  TODO - get md5s
    var players = [];
    featureNames.forEach(function (name) {
      var data = $scope[name];
      if ((typeof data !== 'undefined') && (data !== '')) {
        featureSet.push(data);
      }
    });
    $http.post($scope.url, {'players': players, 'features': featureSet}).success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      // TODO
      console.log(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.log(data + status + headers + config);
    });
  };

  //  Initialize
  $scope.setSinglePlayer();
});
