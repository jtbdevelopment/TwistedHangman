'use strict';

angular.module('twistedHangmanApp').controller('CoreInviteCtrl',
  ['$modalInstance', '$scope', 'invitableFriends', 'jtbFacebook',
    function ($modalInstance, $scope, invitableFriends, jtbFacebook) {
      $scope.invitableFriends = invitableFriends;
      $scope.chosenFriends = [];
      $scope.invite = function () {
        var ids = [];
        angular.forEach($scope.chosenFriends, function (chosen) {
          ids.push(chosen.id);
        });
        jtbFacebook.inviteFriends(ids);
        $modalInstance.close();
      };
      $scope.cancel = function () {
        $modalInstance.dismiss();
      };
    }]);

