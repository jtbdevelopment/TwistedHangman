'use strict';

angular.module('twistedHangmanApp').controller('AdminCtrl',
  ['$scope', '$http', '$location', 'jtbPlayerService',
    function ($scope, $http, $location, jtbPlayerService) {
      $scope.searchText = '';
      $scope.players = [];
      $scope.selected = {};
      $scope.playerCount = 0;
      $scope.last24hours = 0;
      $scope.last7days = 0;
      $scope.last30days = 0;

      var time = Math.floor((new Date()).getTime() / 1000);
      var dayInSeconds = 86400;
      var time24 = time - (dayInSeconds);
      var time7 = time - (dayInSeconds * 7);
      var time30 = time - (dayInSeconds * 30);

      $http.get('/api/player/admin/playerCount').success(function (data) {
        $scope.playerCount = data;
      });

      $http.get('/api/player/admin/gamesSince/' + time24).success(function (data) {
        $scope.last24hours = data;
      });

      $http.get('/api/player/admin/gamesSince/' + time7).success(function (data) {
        $scope.last7days = data;
      });

      $http.get('/api/player/admin/gamesSince/' + time30).success(function (data) {
        $scope.last30days = data;
      });

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
