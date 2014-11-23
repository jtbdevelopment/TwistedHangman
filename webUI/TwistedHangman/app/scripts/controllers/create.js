'use strict';

//  TODO - test


var baseUrl = '/api/player/';
var restUrl = '/new';

angular.module('twistedHangmanApp').controller('CreateCtrl', function ($rootScope, $scope, twGameFeatureService, twCurrentPlayerService, $http) {
  $scope.url = baseUrl + twCurrentPlayerService.currentID() + restUrl;
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
  $scope.thieving = 'Thieving';
  $scope.drawGallows = '';
  $scope.drawFace = '';
  $scope.gamePace = '';

  $scope.setSinglePlayer = function () {
    $scope.playerCount = 'SinglePlayer';
    $scope.players = [];
    $scope.gamePace = '';
    $scope.puzzleSetter = 'SystemPuzzles';
    $scope.winners = 'SingleWinner';
    $scope.h2hEnabled = false;
    $scope.alternatingEnabled = false;
    $scope.allFinishedEnabled = false;
    $scope.turnBasedEnabled = false;
  };

  $scope.setTwoPlayers = function () {
    $scope.playerCount = 'TwoPlayer';
    if ($scope.players.length > 1) {
      $scope.players = [];
    }
    $scope.h2hEnabled = true;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
  };

  $scope.setThreePlayers = function () {
    $scope.playerCount = 'ThreePlus';
    if ($scope.puzzleSetter === '') {
      $scope.puzzleSetter = 'SystemPuzzles';
    }
    $scope.h2hEnabled = false;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
  };

  $scope.createGame = function () {
    var featureNames = ['puzzleSetter', 'playerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace'];
    var featureSet = [];
    var players = [];
    featureNames.forEach(function (name) {
      var data = $scope[name];
      if ((typeof data !== 'undefined') && (data !== '')) {
        featureSet.push(data);
      }
    });
    $http.post($scope.url, {'players': players, 'features': featureSet}).success(function (data) {
      $rootScope.$broadcast('refreshGames', '');
      console.log(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.log(data + status + headers + config);
    });
  };

  //  Initialize
  $scope.setSinglePlayer();
});
