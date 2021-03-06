'use strict';

angular.module('twistedHangmanApp').controller('MenuCtrl',
    ['$scope', 'jtbGameCache', 'jtbGameClassifier', 'twGameDetails', '$timeout', '$animate',
        function ($scope, jtbGameCache, jtbGameClassifier, twGameDetails, $timeout, $animate) {
            var controller = this;

            controller.phases = [];
            controller.phaseGlyphicons = jtbGameClassifier.getIcons();
            controller.phaseStyles = {};
            controller.phaseLabels = {};
            controller.phaseDescriptions = {};
            controller.phaseCollapsed = {};
            controller.games = {};
            controller.descriptions = {};

            angular.forEach(jtbGameClassifier.getClassifications(), function (value) {
                controller.phases.push(value);
                controller.phaseLabels[value] = value;
                controller.phaseDescriptions[value] = value;
                controller.games[value] = [];
                controller.phaseCollapsed[value] = false;
                controller.phaseStyles[value] = value.toLowerCase().split(' ').join('-').split('.').join('');
            });

            function updateGames() {
                angular.forEach(controller.phases, function (phase) {
                    controller.games[phase] = jtbGameCache.getGamesForPhase(phase);
                    angular.forEach(controller.games[phase], function (game) {
                        controller.descriptions[game.id] = twGameDetails.shortGameDescription(game);
                    });
                });
            }

            function shakeIt(game) {
                $timeout(function () {
                    var buttonId = '#' + game.id;
                    var prop = angular.element(buttonId);
                    if (angular.isDefined(prop)) {
                        $animate.addClass(prop, 'animated shake').then(function () {
                            var prop = angular.element(buttonId);
                            $animate.removeClass(prop, 'animated shake');
                        });
                    }
                }, 200);  // give render time
            }

            $scope.$on('gameCachesLoaded', function () {
                updateGames();
            });

            $scope.$on('gameRemoved', function () {
                updateGames();
            });

            $scope.$on('gameAdded', function (event, game) {
                updateGames();
                shakeIt(game);
            });

            $scope.$on('gameUpdated', function (event, oldGame, newGame) {
                updateGames();
                if (oldGame.gamePhase !== newGame.gamePhase) {
                    shakeIt(newGame);
                }
            });
        }
    ]
);
