'use strict';

angular.module('twistedHangmanApp').factory('twGameFeatureService', function ($http, $q) {
  return {
    features: function () {
      //  TODO - local cookie?
      return $http.get('/api/player/features', {cache: true}).then(function (response) {
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

