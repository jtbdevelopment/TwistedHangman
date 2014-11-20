'use strict';

//  TODO - different players
var root = '/api/player/54581119a962efc67353a45c/games/';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */


var phases = ['Playing', 'Setup', 'Challenge', 'Rematch', 'Declined', 'Rematched'];
phases.map(function (phase) {
  var name = phase + 'Games';
  angular.module('twistedHangmanApp').controller(name, function ($scope, $http) {
    $scope.games = [];

    var url = root + phase;
    var params = {params: {pageSize: 100}};
    $http.get(url, params).success(function (data, status, headers, config) {
      $scope.games = data;
    }).error(function (data, status, headers, config) {
      //  TODO
    });
  });
});
