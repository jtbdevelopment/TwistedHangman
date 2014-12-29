/*global $:false */
'use strict';

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
    ['$scope', '$location', '$animate', '$timeout', 'twGamePhaseService', 'twGameCache',
      function ($scope, $location, $animate, $timeout, twGamePhaseService, twGameCache) {
        $scope.games = [];
        $scope.style = phase.toLowerCase() + 'Button';
        $scope.glyph = 'glyphicon-' + glyph;
        $scope.label = '';
        $scope.phase = phase;
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

        $scope.$on('phaseChangeAlert', function (event, game) {
          //  Brief timeout to allow event to fully propagate so data is current
          $timeout(function () {
            if (game.gamePhase === phase) {
              var buttonId = '#' + game.id;
              var prop = angular.element(buttonId);
              if (angular.isDefined(prop)) {
                $animate.addClass(prop, 'animated shake').then(function () {
                  var prop = angular.element(buttonId);
                  //  TODO - not sure why but angular remove not working - jquery for now
                  $(buttonId).removeClass('animated');
                  $(buttonId).removeClass('shake');
                  $animate.removeClass(prop, 'animated shake');
                });
              }
            }
          }, 1);
        });
      }]);
});

