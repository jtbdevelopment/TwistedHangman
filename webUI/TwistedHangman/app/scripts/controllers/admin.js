'use strict';

angular.module('twistedHangmanApp').controller('AdminCtrl',
  ['$scope', '$http', '$location', 'twPlayerService',
    function ($scope, $http, $location, twPlayerService) {
      $scope.searchText = '';
      $scope.players = [];
      $scope.selected = {};
      $http.get('/api/player/admin').success(function (data) {
        $scope.players = data;

      }).error(function (data, status, headers, config) {
        console.error(data + status + headers + config);
        $location.path('/error');
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
