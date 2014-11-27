'use strict';

angular.module('twistedHangmanApp').factory('twCurrentPlayerService', function ($http, $q) {
//  TODO - different ids
  var pid = 'MANUAL1';

  return {
    currentID: function () {
      return pid;
    },
    currentPlayerBaseURL: function () {
      return '/api/player/' + pid;
    },
    friends: function () {
      //  TODO - local cookie?
      return $http.get(this.currentPlayerBaseURL() + '/friends', {cache: true}).then(function (response) {
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
})
;

