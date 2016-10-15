'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
var CURRENT_VERSION = 1.3;
var RELEASE_NOTES = 'Reorganized the game lists.';
angular.module('twistedHangmanApp')
    .controller('MainCtrl',
        ['$scope', 'jtbAppLongName', 'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapVersionNotesService',
            function ($scope, jtbAppLongName, jtbGameCache, jtbPlayerService, jtbBootstrapVersionNotesService) {
                $scope.playerGreeting = '';
                $scope.createRefreshEnabled = false;
                $scope.showAdmin = false;
                $scope.showLogout = false;
                $scope.currentPlayer = {};
                $scope.currentPlayer = {gameSpecificPlayerAttributes: {freeGamesUsedToday: 0}};
                $scope.includeTemplate = 'views/empty.html';
                $scope.appName = jtbAppLongName;
                console.log(jtbGameCache);

                $scope.logout = function () {
                    $scope.includeTemplate = 'views/empty.html';
                    jtbPlayerService.signOutAndRedirect();
                };

                $scope.$on('playerLoaded', function () {
                    $scope.currentPlayer = jtbPlayerService.currentPlayer();
                    $scope.playerGreeting = 'Welcome ' + $scope.currentPlayer.displayName;
                    $scope.showAdmin = $scope.currentPlayer.adminUser || $scope.showAdmin;
                    $scope.showLogout = $scope.currentPlayer.source === 'MANUAL';
                    $scope.createRefreshEnabled = true;
                    $scope.includeTemplate = 'views/sidebar.html';
                    jtbBootstrapVersionNotesService.displayVersionNotesIfAppropriate(CURRENT_VERSION, RELEASE_NOTES);
                });

            }
        ]
    );
