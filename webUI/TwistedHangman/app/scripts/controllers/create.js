'use strict';

var SINGLE_PLAYER = 'SinglePlayer';
var TWO_PLAYERS = 'TwoPlayer';
var MULTI_PLAYER = 'ThreePlus';
var SYSTEM_PUZZLES = 'SystemPuzzles';

angular.module('twistedHangmanApp').controller('CreateCtrl', function ($rootScope, $scope, twGameFeatureService, twCurrentPlayerService, $http, $window) {
  $scope.url = twCurrentPlayerService.currentPlayerBaseURL() + '/new';
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  }, function () {
    //  TODO
  });
  $scope.friends = [];
  twCurrentPlayerService.friends().then(function (data) {
    angular.forEach(data, function (displayName, hash) {
      $scope.friends.push({
        md5: hash,
        name: displayName,
        selected: false,
        enabled: false
      });
    });
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

  function calcSubmitEnabled() {
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
  }

  function calcFriendsEnabledAndSelected() {
    switch ($scope.playerCount) {
      case    TWO_PLAYERS:
        var moreFriendsEnabled = ($scope.players.length < 1);
        $scope.friends.forEach(function (friend) {
          if (!friend.selected) {
            friend.enabled = moreFriendsEnabled;
          }
        });
        break;
      case    MULTI_PLAYER:
        $scope.friends.forEach(function (friend) {
          friend.enabled = true;
        });
        break;
      default:
        $scope.friends.forEach(function (friend) {
          friend.enabled = false;
          friend.selected = false;
        });
        break;
    }
  }

  $scope.changePlayer = function () {
    $scope.players = [];
    $scope.friends.forEach(function (friend) {
      if (friend.selected) {
        $scope.players.push(friend.md5);
      }
    });
    calcFriendsEnabledAndSelected();
    calcSubmitEnabled();
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
    calcSubmitEnabled();
    calcFriendsEnabledAndSelected();
  };

  $scope.setTwoPlayers = function () {
    $scope.playerCount = TWO_PLAYERS;
    if ($scope.players.length > 1) {
      $scope.players = [];
      $scope.friends.forEach(function (friend) {
        friend.selected = false;
      });
    }
    $scope.h2hEnabled = true;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
    calcSubmitEnabled();
    calcFriendsEnabledAndSelected();
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
    calcSubmitEnabled();
    calcFriendsEnabledAndSelected();
  };

  $scope.createGame = function () {
    var featureNames = ['puzzleSetter', 'playerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace', 'winners'];
    var featureSet = [];
    featureNames.forEach(function (name) {
      var data = $scope[name];
      if ((typeof data !== 'undefined') && (data !== '')) {
        featureSet.push(data);
      }
    });
    $http.post($scope.url, {'players': $scope.players, 'features': featureSet}).success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      $window.location.replace('#/show/' + data.id);
      // TODO
      console.log(data);
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  //  Initialize
  $scope.setSinglePlayer();
});
