'use strict';

angular.module('twistedHangmanApp').controller('ErrorCtrl',
    ['$uibModalInstance', '$scope', 'message',
        function ($uibModalInstance, $scope, message) {
            $scope.message = message;
            $scope.ok = function () {
                $uibModalInstance.dismiss();
            };
        }]);
