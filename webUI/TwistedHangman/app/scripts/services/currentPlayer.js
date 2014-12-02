'use strict';


angular.module('twistedHangmanApp').factory('twCurrentPlayerService', ['$http', '$q', function ($http, $q) {
//  TODO - different ids
  console.warn('Initializing Current Player');
  var realPID = 'MANUAL1';
  var simulatedPID = 'MANUAL1';
  var BASE_PLAYER_URL = '/api/player/';
  var FRIENDS_PATH = '/friends';

  return {
    //  TODO - review, lockdown and test all this real and override stuff
    overridePID: function (newpid) {
      //  TODO - admin and validate
      simulatedPID = newpid;
    },
    realPID: function () {
      return realPID;
    },
    realPlayerBaseURL: function () {
      return BASE_PLAYER_URL + realPID;
    },
    realPlayerFriends: function () {
      //  TODO - local cookie?
      return $http.get(this.realPlayerBaseURL() + FRIENDS_PATH, {cache: true}).then(function (response) {
        if (response.status === 200) {
          return response.data;
        } else {
          $q.reject(response);
        }
      }, function (response) {
        $q.reject(response);
      });
    },
    realPlayer: function () {
      //  TODO - local cookie?
      return $http.get(this.realPlayerBaseURL(), {cache: true}).then(function (response) {
        if (response.status === 200) {
          return response.data;
        } else {
          $q.reject(response);
        }
      }, function (response) {
        $q.reject(response);
      });
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
        if (response.status === 200) {
          return response.data;
        } else {
          $q.reject(response);
        }
      }, function (response) {
        $q.reject(response);
      });
    },
    currentPlayer: function () {
      //  TODO - local cookie?
      return $http.get(this.currentPlayerBaseURL(), {cache: true}).then(function (response) {
        if (response.status === 200) {
          return response.data;
        } else {
          $q.reject(response);
        }
      }, function (response) {
        $q.reject(response);
      });
    }
  };
}]);

