'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */

var phasesAndSymbols = {
  Playing: 'play',
  Setup: 'comment',
  Challenge: 'inbox',
  Rematch: 'repeat',
  Declined: 'remove',
  Rematched: 'flag'
};
angular.forEach(phasesAndSymbols, function (glyph, phase) {
  var name = phase + 'Games';
  angular.module('twistedHangmanApp').controller(name, function ($scope, $http, twCurrentPlayerService) {
    $scope.games = [];

    $scope.style = phase.toLowerCase() + 'Button';
    //  TODO - images
    $scope.glyph = 'glyphicon-' + glyph;
    $scope.label = phase;
    var url = twCurrentPlayerService.currentPlayerBaseURL() + '/games/' + phase;
    var params = {params: {pageSize: 100}};

    $scope.reload = function () {
      $http.get(url, params).success(function (data) {
        $scope.games = data;
      }).error(function (data, status, headers, config) {
        //  TODO
        console.log(data + status + headers + config);
      });
    };
    $scope.reload();

    $scope.$on('refreshGames', function (event, data) {
      if (data === '' || data === $scope.label) {
        $scope.reload();
      }
    });
  });
});

