'use strict';

angular.module('twistedHangmanApp').controller('PopupAdCtrl',
  ['$modalInstance', '$scope', '$timeout',
    function ($modalInstance, $scope, $timeout) {
      $scope.message = '';
      $scope.showContinue = false;
      $scope.adCounts = -1;
      $scope.play = function () {
        $modalInstance.close();
      };

      $timeout(adCounter, 0);
      function adCounter() {
        $scope.adCounts += 1;
        if ($scope.adCounts < 5) {
          $scope.message = 'Continue in ' + (5 - $scope.adCounts) + ' seconds...';
          $timeout(adCounter, 1000);
        } else {
          $scope.showContinue = true;
          $scope.message = '';
        }
      }
    }]);

