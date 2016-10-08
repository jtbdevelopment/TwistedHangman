'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
    .controller('MainCtrl',
        ['$rootScope', '$scope', '$location', '$timeout', 'jtbGameCache', 'jtbPlayerService', 'twGameDetails', 'twGameAlerts',
            function ($rootScope, $scope, $location, $timeout, jtbGameCache, jtbPlayerService, twGameDetails, twGameAlerts) {
                if (1 === 2) {
                    console.log(JSON.stringify(twGameAlerts));
                }
                $scope.playerGreeting = '';
                $scope.alerts = [];
                $scope.createRefreshEnabled = false;
                $scope.showAdmin = false;
                $scope.showLogout = false;
                $scope.currentPlayer = {};
                $scope.currentPlayer = {gameSpecificPlayerAttributes: {freeGamesUsedToday: 0}};
                $scope.includeTemplate = 'views/empty.html';
                console.log(jtbGameCache);

                function gamePath(id) {
                    return '/show/' + id;
                }

                $scope.goToGame = function (index) {
                    if (index < 0 || index >= $scope.alerts.length) {
                        return;
                    }
                    var game = $scope.alerts[index];
                    $scope.alerts.splice(index, 1);
                    $location.path(gamePath(game.id));
                };

                $scope.refreshGames = function () {
                    $rootScope.$broadcast('refreshGames', '');
                };

                $scope.logout = function () {
                    $scope.includeTemplate = 'views/empty.html';
                    jtbPlayerService.signOutAndRedirect();
                };

                $scope.$on('playerLoaded', function () {
                    $scope.alerts = [];
                    $scope.currentPlayer = jtbPlayerService.currentPlayer();
                    $scope.playerGreeting = 'Welcome ' + $scope.currentPlayer.displayName;
                    $scope.showAdmin = $scope.currentPlayer.adminUser || $scope.showAdmin;
                    $scope.showLogout = $scope.currentPlayer.source === 'MANUAL';
                    $scope.createRefreshEnabled = true;
                    $scope.includeTemplate = 'views/sidebar.html';
                });

                function generateAlert(gameId, alertMessage) {
                    $timeout(function () {
                        //  Don't show alerts if game is currently on display
                        if ($location.path().indexOf(gamePath(gameId)) < 0) {
                            $scope.alerts.splice(0, 0, {
                                id: gameId,
                                message: alertMessage
                            });
                        }
                    }, 1);
                }

                $scope.$on('roundOverAlert', function (event, game) {
                    var score = twGameDetails.gameScoreForPlayer(game, jtbPlayerService.currentPlayer().md5);
                    if (score > 0) {
                        generateAlert(game.id, 'Round ended and you scored!');
                    }
                    else if (score === 0) {
                        generateAlert(game.id, 'Round ended and you drew.');
                    } else {
                        generateAlert(game.id, 'Round ended and you lost!');
                    }
                });

                $scope.$on('playAlert', function (event, game) {
                    if (game.features.indexOf('TurnBased') >= 0) {
                        generateAlert(game.id, 'It\'s you\'re turn!');
                    } else {
                        generateAlert(game.id, 'Time to play!');
                    }
                });

                $scope.$on('challengedAlert', function (event, game) {
                    generateAlert(game.id, 'You\'ve been challenged!');
                });

                $scope.$on('declinedAlert', function (event, game) {
                    generateAlert(game.id, 'A challenge was declined!');
                });

                $scope.$on('quitAlert', function (event, game) {
                    generateAlert(game.id, 'A game was quit!');
                });

                $scope.$on('setupAlert', function (event, game) {
                    generateAlert(game.id, 'You need to enter a puzzle!');
                });
            }]);
