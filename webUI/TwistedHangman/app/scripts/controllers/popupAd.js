'use strict';

angular.module('twistedHangmanApp').controller('PopupAdCtrl',
  ['$modalInstance', '$scope',
    function ($modalInstance, $scope) {
      $scope.play = function () {
        $modalInstance.close();
      };
    }]);

