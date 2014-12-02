'use strict';

angular.module('twistedHangmanApp').factory('twGamePhaseService', ['$http', '$q', function ($http, $q) {
  return {
    phases: function () {
      //  TODO - local cookie?
      return $http.get('/api/player/phases', {cache: true}).then(function (response) {
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

