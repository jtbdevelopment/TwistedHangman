'use strict';

angular.module('twistedHangmanApp').factory('twCurrentPlayerService', function ($http, $q) {
//  TODO - different ids
  var pid = 'MANUAL1';

  return {
    currentID: function () {
      return pid;
    },
    currentPlayerBaseURL: function () {
      return '/api/player/' + pid + '/';
    },
    currentPlayer: function () {
      //  TODO - local cookie?
      return $http.get('/api/player/' + pid, {cache: true}).then(function (response) {
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

