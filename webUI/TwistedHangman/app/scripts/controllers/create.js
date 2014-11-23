'use strict';

//  TODO - test


angular.module('twistedHangmanApp').controller('CreateCtrl', function () {
});

var baseUrl = '/api/player/';
var restUrl = '/new';

angular.module('twistedHangmanApp').controller('SinglePlayerCtrl', function ($scope, twGameFeatureService, $window, twCurrentPlayerService, $http) {
  $scope.puzzleSetter = 'SystemPuzzles';
  $scope.winners = 'SingleWinner';
  $scope.playerCount = 'SinglePlayer';
  $scope.thieving = 'Thieving';
  $scope.drawGallows = '';
  $scope.drawFace = '';
  $scope.gamePace = '';
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
  $scope.players = [];
  $scope.url = baseUrl + twCurrentPlayerService.currentID() + restUrl;

  $scope.createGame = function () {
    $window.alert('puzzleSetter=' + $scope.puzzleSetter +
      ', winners=' + $scope.winners +
      ', playerCount=' + $scope.playerCount +
      ', thieving=' + $scope.thieving +
      ', drawGallows=' + $scope.drawGallows +
      ', drawFace=' + $scope.drawFace +
      ', gamePace=' + $scope.gamePace
    );
    var featureNames = ['puzzleSetter', 'playerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace'];
    var featureSet = [];
    featureNames.forEach(function (name) {
      var data = $scope[name];
      if ((typeof data !== 'undefined') && (data !== '')) {
        featureSet.push(data);
      }
    });
    var players = [];
    $window.alert(featureSet);
    $window.alert(players);
    $window.alert($scope.url);
    $window.alert(twCurrentPlayerService.currentID());
    $http.post($scope.url, {'players': players, 'features': featureSet});
  };
});

angular.module('twistedHangmanApp').controller('TwoPlayerCtrl', function ($scope, twGameFeatureService) {
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
});

angular.module('twistedHangmanApp').controller('MultiPlayerCtrl', function ($scope, twGameFeatureService) {
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
});
