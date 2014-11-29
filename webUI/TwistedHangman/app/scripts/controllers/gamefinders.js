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
  Challenged: 'inbox',
  RoundOver: 'repeat',
  Declined: 'remove',
  NextRoundStarted: 'ok-sign',
  Quit: 'flag'
};
angular.forEach(phasesAndSymbols, function (glyph, phase) {
  var name = phase + 'Games';
  angular.module('twistedHangmanApp').controller(name, function ($scope, $http, twCurrentPlayerService, twGamePhaseService) {
    $scope.games = [];

    $scope.style = phase.toLowerCase() + 'Button';
    $scope.glyph = 'glyphicon-' + glyph;
    $scope.label = '';
    twGamePhaseService.phases().then(function (phases) {
      $scope.label = phases[phase][1];
    }, function () {
      //  TODO
    });
    $scope.url = twCurrentPlayerService.currentPlayerBaseURL() + '/games/' + phase;
    var params = {params: {pageSize: 100}};

    $scope.reload = function () {
      $http.get($scope.url, params).success(function (data) {
        $scope.games = data;
      }).error(function (data, status, headers, config) {
        //  TODO
        console.log(data + status + headers + config);
      });
    };

    $scope.$on('refreshGames', function (event, data) {
      if (data === '' || data === phase) {
        $scope.reload();
      }
    });

    $scope.$on('playerSwitch', function () {
      $scope.url = twCurrentPlayerService.currentPlayerBaseURL() + '/games/' + phase;
      $scope.reload();
    });

    $scope.reload();
  });
});

