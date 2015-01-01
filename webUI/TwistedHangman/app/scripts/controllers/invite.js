'use strict';

angular.module('twistedHangmanApp').controller('InviteCtrl',
  ['$modalInstance', '$scope', 'invitable', 'twFacebook',
    function ($modalInstance, $scope, invitableFriends, twFacebook) {
      $scope.invitableFriends = invitableFriends;
      $scope.chosenFriends = [];
      $scope.invite = function () {
        console.error(JSON.stringify($scope.chosenFriends));
        $modalInstance.close();
      };
      $scope.cancel = function () {
        $modalInstance.dismiss();
      };
    }]);

