'use strict';

angular.module('twistedHangmanApp').controller('ConfirmCtrl',
    ['$uibModalInstance', '$scope',
        function ($uibModalInstance, $scope) {
            $scope.confirm = function () {
                $uibModalInstance.close();
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss();
            };
        }]);
