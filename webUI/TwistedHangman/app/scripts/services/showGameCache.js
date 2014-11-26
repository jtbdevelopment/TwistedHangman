'use strict';

angular.module('twistedHangmanApp').factory('twShowGameCache', ['$cacheFactory', function ($cacheFactory) {
  return $cacheFactory('twShowGameCache');
}]);

