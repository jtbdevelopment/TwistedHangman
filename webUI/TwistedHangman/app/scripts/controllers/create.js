'use strict';

var SINGLE_PLAYER = 'SinglePlayer';
var TWO_PLAYERS = 'TwoPlayer';
var MULTI_PLAYER = 'ThreePlus';
var SYSTEM_PUZZLES = 'SystemPuzzles';

//  TODO - move
angular.module('twistedHangmanApp').filter('propsFilter', function () {
  return function (items, props) {
    var out = [];

    if (angular.isArray(items)) {
      items.forEach(function (item) {
        var itemMatches = false;

        var keys = Object.keys(props);
        for (var i = 0; i < keys.length; i++) {
          var prop = keys[i];
          var text = props[prop].toLowerCase();
          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
            itemMatches = true;
            break;
          }
        }

        if (itemMatches) {
          out.push(item);
        }
      });
    } else {
      // Let the output be the input untouched
      out = items;
    }

    return out;
  };
});

angular.module('twistedHangmanApp').controller('CreateCtrl', function ($rootScope, $scope, twGameFeatureService, twCurrentPlayerService, $http, $location) {
  $scope.url = twCurrentPlayerService.currentPlayerBaseURL() + '/new';
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  }, function () {
    //  TODO
  });
  $scope.friend = {};
  $scope.friends = [];
  twCurrentPlayerService.currentPlayerFriends().then(function (data) {
    angular.forEach(data, function (displayName, hash) {
      var friend = {
        md5: hash,
        name: displayName
      };
      $scope.friends.push(friend);
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
  $scope.playerChoices = [];
  $scope.playerCount = '';
  $scope.playersEnabled = false;

  function calcEnabledFields() {
    switch ($scope.playerCount) {
      case    SINGLE_PLAYER:
        $scope.submitEnabled = ($scope.players.length === 0);
        $scope.playersEnabled = false;
        break;
      case    TWO_PLAYERS:
        $scope.submitEnabled = ($scope.players.length === 1);
        $scope.playersEnabled = true;
        break;
      case    MULTI_PLAYER:
        $scope.submitEnabled = ($scope.players.length > 1);
        $scope.playersEnabled = true;
        break;
    }
  }

  $scope.addPlayer = function (item) {
    $scope.players.push(item.md5);
    calcEnabledFields();
  };
  $scope.removePlayer = function (item) {
    var index = $scope.players.indexOf(item.md5);
    if (index >= 0) {
      $scope.players.splice(index, 1);
    }
    calcEnabledFields();
  };

  $scope.clearPlayers = function () {
    $scope.players = [];
    $scope.playerChoices = [];
    calcEnabledFields();
  };

  $scope.setSinglePlayer = function () {
    $scope.playerCount = SINGLE_PLAYER;
    $scope.playerChoices = [];
    $scope.players = [];
    $scope.gamePace = '';
    $scope.wordPhraseSetter = SYSTEM_PUZZLES;
    $scope.winners = 'SingleWinner';
    $scope.h2hEnabled = false;
    $scope.alternatingEnabled = false;
    $scope.allFinishedEnabled = false;
    $scope.turnBasedEnabled = false;
    calcEnabledFields();
  };

  $scope.setTwoPlayers = function () {
    $scope.playerCount = TWO_PLAYERS;
    if ($scope.players.length > 1) {
      $scope.playerChoices = [];
      $scope.players = [];
    }
    $scope.h2hEnabled = true;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
    calcEnabledFields();
  };

  $scope.setThreePlayers = function () {
    $scope.playerCount = MULTI_PLAYER;
    if ($scope.wordPhraseSetter === '') {
      $scope.wordPhraseSetter = SYSTEM_PUZZLES;
    }
    $scope.h2hEnabled = false;
    $scope.alternatingEnabled = true;
    $scope.allFinishedEnabled = true;
    $scope.turnBasedEnabled = true;
    calcEnabledFields();
  };

  $scope.createGame = function () {
    var featureNames = ['wordPhraseSetter', 'playerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace', 'winners'];
    var featureSet = [];
    featureNames.forEach(function (name) {
      var data = $scope[name];
      if ((angular.isDefined(data)) && (data !== '')) {
        featureSet.push(data);
      }
    });
    var playersAndFeatures = {'players': $scope.players, 'features': featureSet};
    $http.post($scope.url, playersAndFeatures).success(function (data) {
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      $location.path('/show/' + data.id);
      // TODO
    }).error(function (data, status, headers, config) {
      //  TODO
      console.error(data + status + headers + config);
    });
  };

  //  Initialize
  $scope.setSinglePlayer();
});
