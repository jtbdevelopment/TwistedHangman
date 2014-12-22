'use strict';

angular.module('twistedHangmanApp').controller('CreateCtrl',
  ['$scope', 'twGameCache', 'twGameFeatureService', 'twPlayerService', '$http', '$location',
    function ($scope, twGameCache, twGameFeatureService, twPlayerService, $http, $location) {

      var SINGLE_PLAYER = 'SinglePlayer';
      var TWO_PLAYERS = 'TwoPlayer';
      var MULTI_PLAYER = 'ThreePlus';
      var SYSTEM_PUZZLES = 'SystemPuzzles';

      function calcSubmitEnabled() {
        switch ($scope.desiredPlayerCount) {
          case    SINGLE_PLAYER:
            $scope.submitEnabled = ($scope.playerChoices.length === 0);
            break;
          case    TWO_PLAYERS:
            $scope.submitEnabled = ($scope.playerChoices.length === 1);
            break;
          case    MULTI_PLAYER:
            $scope.submitEnabled = ($scope.playerChoices.length > 1);
            break;
        }
      }

      $scope.alerts = [];
      $scope.featureData = {};
      twGameFeatureService.features().then(function (data) {
        $scope.featureData = data;
      }, function () {
        //  TODO - route to error page?
      });

      $scope.friends = [];
      twPlayerService.currentPlayerFriends().then(function (data) {
        angular.forEach(data, function (displayName, hash) {
          var friend = {
            md5: hash,
            displayName: displayName
          };
          $scope.friends.push(friend);
        });
      }, function () {
        //  TODO - route to error page?
      });

      $scope.friend = {};
      $scope.thieving = 'Thieving';
      $scope.drawGallows = '';
      $scope.drawFace = 'DrawFace';
      $scope.gamePace = 'Live';
      $scope.submitEnabled = false;
      $scope.playerChoices = [];
      $scope.desiredPlayerCount = '';
      $scope.playersEnabled = false;
      $scope.$watchCollection('playerChoices', calcSubmitEnabled);

      $scope.clearPlayers = function () {
        $scope.playerChoices = [];
        calcSubmitEnabled();
      };

      $scope.setSinglePlayer = function () {
        $scope.playersEnabled = false;
        $scope.desiredPlayerCount = SINGLE_PLAYER;
        $scope.gamePace = 'Live';
        $scope.wordPhraseSetter = SYSTEM_PUZZLES;
        $scope.winners = 'SingleWinner';
        $scope.h2hEnabled = false;
        $scope.alternatingEnabled = false;
        $scope.allFinishedEnabled = false;
        $scope.turnBasedEnabled = false;
        $scope.playerChoices = [];
        calcSubmitEnabled();
      };

      $scope.setTwoPlayers = function () {
        $scope.playersEnabled = true;
        $scope.desiredPlayerCount = TWO_PLAYERS;
        $scope.h2hEnabled = true;
        $scope.alternatingEnabled = true;
        $scope.allFinishedEnabled = true;
        $scope.turnBasedEnabled = true;
        if ($scope.playerChoices.length > 1) {
          $scope.playerChoices = [];
        }
        calcSubmitEnabled();
      };

      $scope.setThreePlayers = function () {
        $scope.playersEnabled = true;
        $scope.desiredPlayerCount = MULTI_PLAYER;
        $scope.h2hEnabled = false;
        $scope.alternatingEnabled = true;
        $scope.allFinishedEnabled = true;
        $scope.turnBasedEnabled = true;
        if ($scope.wordPhraseSetter === 'Head2Head') {
          $scope.wordPhraseSetter = SYSTEM_PUZZLES;
        }
        calcSubmitEnabled();
      };

      $scope.createGame = function () {
        var featureNames = ['wordPhraseSetter', 'desiredPlayerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace', 'winners'];
        var featureSet = [];
        featureSet = featureSet.concat(featureNames.map(function (name) {
            var data = $scope[name];
            if ((angular.isDefined(data)) && (data !== '')) {
              return data;
            }
            return '';
          }
        ).filter(function (item) {
            return item !== '';
          }));

        var players = $scope.playerChoices.map(function (player) {
          return player.md5;
        });
        var playersAndFeatures = {'players': players, 'features': featureSet};
        $http.post(twPlayerService.currentPlayerBaseURL() + '/new', playersAndFeatures).success(function (data) {
          twGameCache.putUpdatedGame(data);
          $location.path('/show/' + data.id);
        }).error(function (data, status, headers, config) {
          $scope.alerts.push({type: 'danger', msg: 'Error creating game - ' + status + ':' + data});
          console.error(data + status + headers + config);
        });
      };

      $scope.closeAlert = function (index) {
        if (angular.isDefined(index) && index >= 0 && index < $scope.alerts.length) {
          $scope.alerts.splice(index, 1);
        }
      };
      //  Initialize
      $scope.setSinglePlayer();
    }
  ]
);
