'use strict';

//  TODO - test


angular.module('twistedHangmanApp').controller('CreateCtrl', function () {
});

angular.module('twistedHangmanApp').controller('SinglePlayerCtrl', function ($scope, twGameFeatureService) {
  $scope.puzzleSetter = 'SystemPuzzles';
  $scope.winners = 'SingleWinner';
  $scope.players = 'SinglePlayer';
  $scope.thieving = 'Thieving';
  $scope.drawGallows = '';
  $scope.drawFace = '';
  $scope.gamePace = '';
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
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
