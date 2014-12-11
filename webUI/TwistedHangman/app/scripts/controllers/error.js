'use strict';

angular.module('twistedHangmanApp').controller('ErrorCtrl',
  ['$modalInstance', '$scope', 'message',
    function ($modalInstance, $scope, message) {
      $scope.message = message;
      $scope.ok = function () {
        $modalInstance.dismiss();
      };
    }]);
