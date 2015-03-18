'use strict';

angular.module('twistedHangmanApp').factory('twGameFeatureService', ['$http', function ($http) {
  return {
    features: function () {
      return $http.get('/api/features', {cache: true}).then(function (response) {
        return response.data;
      });
    }
  };
}]);

