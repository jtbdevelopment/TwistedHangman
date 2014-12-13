'use strict';

angular.module('twistedHangmanApp').controller('AdminCtrl',
  ['$rootScope', '$scope', '$http', '$location', 'twPlayerService',
    function ($rootScope, $scope, $http, $location, twPlayerService) {
      $scope.searchText = '';
      $scope.players = [];
      $scope.selected = {};
      $http.get('/api/player/admin/' + twPlayerService.realPID()).success(function (data) {
        $scope.players = data;

      }).error(function (data, status, headers, config) {
        //  TODO
        console.error(data + status + headers + config);
      });

      $scope.changeUser = function () {
        twPlayerService.overridePID($scope.selected.id);
        $location.path('/');
      };
      $scope.revertUser = function () {
        twPlayerService.overridePID(twPlayerService.realPID());
        $location.path('/');
      };
    }]);
