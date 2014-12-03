'use strict';

angular.module('twistedHangmanApp').controller('ConfirmCtrl',
  ['$modalInstance', '$scope',
    function ($modalInstance, $scope) {
      $scope.confirm = function () {
        $modalInstance.close();
      };

      $scope.cancel = function () {
        $modalInstance.dismiss();
      };
    }]);
