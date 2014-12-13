'use strict';


angular.module('twistedHangmanApp').factory('twPlayerService',
  ['$http', '$rootScope', '$location',
    function ($http, $rootScope, $location) {
      //  TODO - different ids
      var realPID = 'MANUAL1';
      var simulatedPID = 'MANUAL1';
      var BASE_PLAYER_URL = '/api/player/';
      var FRIENDS_PATH = '/friends';

      var simulatedPlayer;
      var realPlayer;

      var service = {
        //  TODO - review, lockdown and test all this real and override stuff
        overridePID: function (newpid) {
          //  TODO - admin and validate
          simulatedPID = newpid;
          loadPlayer();
        },
        realPID: function () {
          return realPID;
        },
        realPlayerBaseURL: function () {
          return BASE_PLAYER_URL + realPID;
        },
        realPlayer: function () {
          return realPlayer;
        },


        currentID: function () {
          return simulatedPID;
        },
        currentPlayerBaseURL: function () {
          return BASE_PLAYER_URL + simulatedPID;
        },
        currentPlayerFriends: function () {
          //  TODO - local cookie?
          return $http.get(this.currentPlayerBaseURL() + FRIENDS_PATH, {cache: true}).then(function (response) {
            return response.data;
          });
        },
        currentPlayer: function () {
          return simulatedPlayer;
        }
      };

      function loadRealPlayer() {
        $http.get(service.realPlayerBaseURL(), {cache: true}).success(function (response) {
          realPlayer = response;
          $rootScope.$broadcast('realPlayerLoaded');
        }).error(function () {
          $location.path('/error');
        });
      }

      function loadPlayer() {
        $http.get(service.currentPlayerBaseURL(), {cache: true}).success(function (response) {
          simulatedPlayer = response;
          $rootScope.$broadcast('playerLoaded');
        }).error(function () {
          $location.path('/error');
        });
      }

      loadRealPlayer();
      loadPlayer();

      return service;
    }]);

