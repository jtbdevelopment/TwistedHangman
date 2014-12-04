'use strict';

angular.module('twistedHangmanApp').factory('twGamePhaseService', ['$http', function ($http) {
  return {
    phases: function () {
      //  TODO - local cookie?
      return $http.get('/api/player/phases', {cache: true}).then(function (response) {
        return response.data;
      });
    }
  };
}]);

