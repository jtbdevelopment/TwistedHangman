'use strict';


angular.module('twistedHangmanApp').factory('twPlayerService',
  ['$http', '$rootScope', '$location',
    function ($http, $rootScope, $location) {
      var realPID = '';
      var simulatedPID = '';
      var BASE_PLAYER_URL = '/api/player/';
      var FRIENDS_PATH = '/friends';

      var simulatedPlayer;

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

      function loadPlayer() {
        var url;
        if (simulatedPID === '') {
          url = '/api/security';
        } else {
          url = service.currentPlayerBaseURL();
        }
        $http.get(url, {cache: true}).success(function (response) {
          simulatedPlayer = response;
          if (realPID === '') {
            realPID = simulatedPlayer.id;
            simulatedPID = realPID;
          }
          $rootScope.$broadcast('playerLoaded');
        }).error(function () {
          $location.path('/error');
        });
      }

      loadPlayer();

      return service;
    }]);

