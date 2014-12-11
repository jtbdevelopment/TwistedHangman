'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */

//  TODO - this would probably work better and more efficiently as a central cache of games
//  TODO - it would also allow publishing real-time etc.
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
  angular.module('twistedHangmanApp').controller(name,
    ['$scope', '$location', 'twGamePhaseService', 'twGameCache',
      function ($scope, $location, twGamePhaseService, twGameCache) {
        $scope.games = [];
        $scope.style = phase.toLowerCase() + 'Button';
        $scope.glyph = 'glyphicon-' + glyph;
        $scope.label = '';
        $scope.hideGames = false;

        $scope.switchHideGames = function () {
          $scope.hideGames = !$scope.hideGames;
        };

        twGamePhaseService.phases().then(function (phases) {
          $scope.label = phases[phase][1];
        }, function () {
          $location.path('/error');
        });

        function loadGames() {
          $scope.games = twGameCache.getGamesForPhase(phase);
        }

        $scope.$on('gameCachesLoaded', function () {
          loadGames();
        });

      }]);
});

