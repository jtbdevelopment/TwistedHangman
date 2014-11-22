'use strict';

//  TODO - test


angular.module('twistedHangmanApp').controller('CreateCtrl', function () {
});

angular.module('twistedHangmanApp').controller('SinglePlayerCtrl', function ($scope, twGameFeatureService) {
  $scope.active = 'single';
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
});

angular.module('twistedHangmanApp').controller('TwoPlayerCtrl', function ($scope, twGameFeatureService) {
  $scope.active = 'two';
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
});

angular.module('twistedHangmanApp').controller('MultiPlayerCtrl', function ($scope, twGameFeatureService) {
  $scope.active = 'multi';
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
  $scope.featureData = {};
  twGameFeatureService.features().then(function (data) {
    $scope.featureData = data;
  });
});
