'use strict';

//  TODO - do we need to publish timing events on various loads so that there are not race conditions
//  i.e. someone trying to use cache before initalized
angular.module('twistedHangmanApp').factory('twGameCache',
  ['$cacheFactory', '$location', '$q', 'twGamePhaseService',
    function ($cacheFactory, $location, $q, twGamePhaseService) {
      var ALL = 'All';
      var cache = $cacheFactory('game-cache');
      var phases = [];

      function initializeCache() {
        cache.removeAll();
        cache.put(ALL, {});
        phases.forEach(function (phase) {
            //  Would be nice to use more caches but can't get keys
            cache.put(phase, {});
            phases.push(phase);
          }
        );
      }

      twGamePhaseService.phases().then(function (phaseMap) {
        phases = [ALL];
        angular.forEach(phaseMap, function (array, phase) {
          phases.push(phase);
        });
        initializeCache();
      }, function () {
        $location.path('/error');
      });

      return {
        clearAll: function () {
          initializeCache();
        },

        initializeGames: function (games) {
          this.clearAll();
          games.forEach(function (game) {
            cache.get(game.gamePhase)[game.id] = game;
            cache.get(ALL)[game.id] = game;
          });
          //  TODO - publish?
        },

        putUpdatedGame: function (game) {
          var phaseCache = cache.get(game.gamePhase);
          var existingGame = cache.get(ALL)[game.id];
          if (angular.isDefined(existingGame)) {
            if (game.lastUpdate <= existingGame.lastUpdate) {
              console.warn('Skipping Stale game update for ' + game.id);
              return;
            }
            //  TODO publish?
          }
          phaseCache[game.id] = game;
          cache.get(ALL)[game.id] = game;
        },

        getAllForPhase: function (phase) {
          return cache.get(phase);
        }
      };
    }]);
