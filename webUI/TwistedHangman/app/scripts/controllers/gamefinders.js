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
        ['$scope', '$animate', '$timeout', 'jtbGamePhaseService', 'jtbGameCache', 'twGameDetails',
            function ($scope, $animate, $timeout, jtbGamePhaseService, jtbGameCache, twGameDetails) {
                $scope.games = [];
                $scope.style = phase.toLowerCase() + 'Button';
                $scope.glyph = 'glyphicon-' + glyph;
                $scope.label = '';
                $scope.phase = phase;
                $scope.hideGames = false;
                $scope.gameDetails = twGameDetails;

                $scope.switchHideGames = function () {
                    $scope.hideGames = !$scope.hideGames;
                };

                jtbGamePhaseService.phases().then(function (phases) {
                    $scope.label = phases[phase][1];
                });

                function loadGames() {
                    $scope.games = jtbGameCache.getGamesForPhase(phase);
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
                                    $animate.removeClass(prop, 'animated shake');
                                });
                            }
                        }
                    }, 1);
                });
            }
        ]);
});

