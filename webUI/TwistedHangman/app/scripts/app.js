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
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/singleplayer', {
        templateUrl: 'views/creategame/singleplayer.html',
        controller: 'SinglePlayerCtrl'
      })
      .when('/twoplayer', {
        templateUrl: 'views/creategame/twoplayer.html',
        controller: 'TwoPlayerCtrl'
      })
      .when('/multiplayer', {
        templateUrl: 'views/creategame/multiplayer.html',
        controller: 'MultiPlayerCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
