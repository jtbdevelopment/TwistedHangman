'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
var CURRENT_VERSION = 1.3;
var RELEASE_NOTES = 'Reorganized the game lists, more games per day and fewer ads.';
angular.module('twistedHangmanApp')
    .controller('MainCtrl',
        ['$rootScope', 'jtbAppLongName', 'jtbPlayerService', 'jtbBootstrapVersionNotesService',
            function ($rootScope, jtbAppLongName, jtbPlayerService, jtbBootstrapVersionNotesService) {
                var controller = this;
                controller.playerGreeting = '';
                controller.createRefreshEnabled = false;
                controller.showAdmin = false;
                controller.showLogout = false;
                controller.currentPlayer = {gameSpecificPlayerAttributes: {freeGamesUsedToday: 0}};
                controller.includeTemplate = 'views/empty.html';
                controller.adTemplate = 'views/ads/empty.html';
                controller.appName = jtbAppLongName;

                controller.logout = function () {
                    controller.includeTemplate = 'views/empty.html';
                    controller.adTemplate = 'views/ads/empty.html';
                    jtbPlayerService.signOutAndRedirect();
                };

                $rootScope.$on('playerLoaded', function () {
                    controller.currentPlayer = jtbPlayerService.currentPlayer();
                    controller.playerGreeting = 'Welcome ' + controller.currentPlayer.displayName;
                    controller.showAdmin = controller.currentPlayer.adminUser || controller.showAdmin;
                    controller.showLogout = controller.currentPlayer.source === 'MANUAL';
                    controller.createRefreshEnabled = true;
                    controller.includeTemplate = 'views/sidebar.html';
                  controller.adTemplate = 'views/ads/empty.html';
                    jtbBootstrapVersionNotesService.displayVersionNotesIfAppropriate(CURRENT_VERSION, RELEASE_NOTES);
                });

            }
        ]
    );
