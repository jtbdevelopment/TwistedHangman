'use strict';

angular.module('twistedHangmanApp').controller('InviteCtrl',
  ['$modalInstance', '$scope', 'invitableFriends', 'twFacebook',
    function ($modalInstance, $scope, invitableFriends, twFacebook) {
      $scope.invitableFriends = invitableFriends;
      $scope.chosenFriends = [];
      $scope.invite = function () {
        var ids = [];
        angular.forEach($scope.chosenFriends, function (chosen) {
          ids.push(chosen.id);
        });
        twFacebook.inviteFriends(ids);
        $modalInstance.close();
      };
      $scope.cancel = function () {
        $modalInstance.dismiss();
      };
    }]);

