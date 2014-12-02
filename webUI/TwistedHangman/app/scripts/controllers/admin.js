'use strict';

angular.module('twistedHangmanApp').controller('AdminCtrl', function ($rootScope, $scope, $http, $location, twCurrentPlayerService) {
  $scope.searchText = '';
  $scope.players = [];
  $scope.selected = {};
  $http.get('/api/player/admin/' + twCurrentPlayerService.realPID()).success(function (data) {
    $scope.players = data;

  }).error(function (data, status, headers, config) {
    //  TODO
    console.error(data + status + headers + config);
  });

  $scope.changeUser = function () {
    twCurrentPlayerService.overridePID($scope.selected.id);
    $location.path('/');
    $rootScope.$broadcast('playerSwitch');
  };
  $scope.revertUser = function () {
    twCurrentPlayerService.overridePID(twCurrentPlayerService.realPID());
    $location.path('/');
    $rootScope.$broadcast('playerSwitch');
  };
});
