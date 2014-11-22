'use strict';

var url = '/api/player/features';

//  TODO - test
//  TODO - promise?
angular.module('twistedHangmanApp').factory('twGameFeatureService', function ($http, $q) {
  return {
    features: function () {
      return $http.get(url, {cache: true}).then(function (response) {
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

