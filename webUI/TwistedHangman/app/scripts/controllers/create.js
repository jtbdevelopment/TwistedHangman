'use strict';

angular.module('twistedHangmanApp').controller('SinglePlayerCtrl', function ($scope) {
  $scope.active = 'single'
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
});

angular.module('twistedHangmanApp').controller('TwoPlayerCtrl', function ($scope) {
  $scope.active = 'two'
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
});

angular.module('twistedHangmanApp').controller('MultiPlayerCtrl', function ($scope) {
  $scope.active = 'multi'
  $scope.baseFeatures = ['SinglePlayer, SystemPuzzles, SingleWinner'];
  $scope.thieving = true;
});
