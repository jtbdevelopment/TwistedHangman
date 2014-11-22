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
      .when('/create', {
        templateUrl: 'views/creategame/create.html',
        controller: 'CreateCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
