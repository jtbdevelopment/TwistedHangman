'use strict';

angular.module('twistedHangmanApp').controller('MenuCtrl',
    ['$scope', 'jtbGameCache', 'jtbGameClassifier', 'twGameDetails',
        function ($scope, jtbGameCache, jtbGameClassifier, twGameDetails) {
            var controller = this;

            controller.phases = [];
            controller.phaseStyles = {};
            controller.phaseLabels = {};
            controller.phaseDescriptions = {};
            controller.phaseCollapsed = {};
            controller.games = {};
            controller.descriptions = {};

            controller.phaseGlyphicons = jtbGameClassifier.getIcons();
            controller.phases = [];
            angular.forEach(jtbGameClassifier.getClassifications(), function (value) {
                controller.phases.push(value);
                controller.phaseLabels[value] = value;
                controller.phaseDescriptions[value] = value;
                controller.games[value] = [];
                controller.phaseCollapsed[value] = false;
                //  TODO - why multiple replace
                controller.phaseStyles[value] = value.toLowerCase().replace(' ', '-').replace(' ', '-').replace('.', '');
            });

            function updateGames() {
                angular.forEach(controller.phases, function (phase) {
                    controller.games[phase] = jtbGameCache.getGamesForPhase(phase);
                    angular.forEach(controller.games[phase], function (game) {
                        controller.descriptions[game.id] = twGameDetails.shortGameDescription(game);
                    });
                });
            }

            $scope.$on('gameCachesLoaded', function () {
                updateGames();
            });

            $scope.$on('gameRemoved', function () {
                updateGames();
            });

            $scope.$on('gameAdded', function () {
                updateGames();
            });

            $scope.$on('gameUpdated', function () {
                updateGames();
            });
        }
    ]
);
