'use strict';

angular.module('twistedHangmanApp').factory('twGamePhaseService', ['$http', function ($http) {
  return {
    phases: function () {
      //  TODO - local cookie?
      return $http.get('/api/phases', {cache: true}).then(function (response) {
        return response.data;
      });
    }
  };
}]);

