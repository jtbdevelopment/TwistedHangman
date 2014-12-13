'use strict';


angular.module('twistedHangmanApp').factory('twPlayerService', ['$http', function ($http) {
//  TODO - different ids
  console.warn('Initializing Current Player');
  var realPID = 'MANUAL1';
  var simulatedPID = 'MANUAL1';
  var BASE_PLAYER_URL = '/api/player/';
  var FRIENDS_PATH = '/friends';

  var service = {
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
        return response.data;
      });
    },
    realPlayer: function () {
      //  TODO - local cookie?
      return $http.get(this.realPlayerBaseURL(), {cache: true}).then(function (response) {
        return response.data;
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
        return response.data;
      });
    },
    currentPlayer: function () {
      return $http.get(this.currentPlayerBaseURL(), {cache: true}).then(function (response) {
        return response.data;
      });
    }
  };

  return service;
}]);

