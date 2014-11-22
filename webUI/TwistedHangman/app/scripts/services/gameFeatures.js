'use strict';

var url = '/api/player/features';

//  TODO - test
//  TODO - promise?
angular.module('twistedHangmanApp').factory('twGameFeatureService', function ($http, $q) {
  var features = {};
  return {
    features: function () {
      if (Object.keys(features).length === 0) {
        return $http.get(url).then(function (response) {
          if (response.status === 200) {
            features = response.data;
            return features;
          } else {
            $q.reject(response);
          }
        }, function (response) {
          $q.reject(response);
        });
      } else {
        var deferred = $q.defer();
        deferred.resolve(features);
        return deferred.promise;
      }
    }
  };
})
;

