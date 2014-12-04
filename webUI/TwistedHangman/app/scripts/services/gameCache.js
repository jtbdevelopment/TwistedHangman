'use strict';

angular.module('twistedHangmanApp').factory('twGameCache',
  ['$cacheFactory', '$location', 'twGamePhaseService',
    function ($cacheFactory, $location, twGamePhaseService) {
      var cache = $cacheFactory('game-cache');

      function initializeCache() {
        cache.removeAll();
        twGamePhaseService.phases().then(function (phases) {
          angular.forEach(phases, function (array, phase) {
              cache.put(phase, []);
            }
          );
        }, function () {
          $location.path('/error');
        });
      }

      initializeCache();

      return {
        put: function (id, key) {
          cache.put(id, key);
        },

        get: function (id) {
          return cache.get(id);
        }
      };
    }]);
