'use strict';

angular.module('twistedHangmanApp').controller('AdminCtrl',
  ['$scope', '$http', '$location', 'jtbPlayerService',
    function ($scope, $http, $location, jtbPlayerService) {
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
        jtbPlayerService.overridePID($scope.selected.id);
        $location.path('/');
      };
      $scope.revertUser = function () {
        jtbPlayerService.overridePID(jtbPlayerService.realPID());
        $location.path('/');
      };
    }]);
