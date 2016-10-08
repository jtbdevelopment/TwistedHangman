'use strict';

/**
 * @ngdoc overview
 * @name twistedHangmanApp
 * @description
 * # twistedHangmanApp
 *
 * Main module of the application.
 */
angular
    .module('twistedHangmanApp', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ui.bootstrap',
        'ui.select',
        'coreGamesUi',
        'coreGamesBootstrapUi'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/main', {
                templateUrl: 'views/main.html'
            })
            .when('/admin', {
                templateUrl: 'views/core-bs/admin/admin.html',
                controller: 'CoreAdminCtrl',
                controllerAs: 'admin'
            })
            .when('/game/:gamePhase/:gameID', {
                templateUrl: 'views/showgame.html',
                controller: 'ShowCtrl'
            })
            .when('/show/:gameID', {
                templateUrl: 'views/showgame.html',
                controller: 'ShowCtrl'
            })
            .when('/create', {
                templateUrl: 'views/create.html',
                controller: 'CreateCtrl'
            })
            .when('/about', {
                templateUrl: 'views/about.html',
                controller: 'AboutCtrl'
            })
            .when('/signin', {
                templateUrl: 'views/core-bs/sign-in/sign-in.html',
                controller: 'CoreBootstrapSignInCtrl',
                controllerAs: 'signIn'
            })
            .when('/signedin', {
                templateUrl: 'views/core-bs/sign-in/signed-in.html',
                controller: 'CoreBootstrapSignedInCtrl',
                controllerAs: 'signedIn'
            })
            .otherwise({
                redirectTo: '/signin'
            });
    });
