'use strict';

//  TODO - do we need to publish timing events on various loads so that there are not race conditions
//  i.e. someone trying to use cache before initalized
angular.module('twistedHangmanApp').factory('twGameCache',
  ['$cacheFactory', '$location', '$q', 'twGamePhaseService',
    function ($cacheFactory, $location, $q, twGamePhaseService) {
      var cache = $cacheFactory('game-cache');
      var allCache = {};
      var phases = [];

      function initializeCache() {
        cache.removeAll();
        twGamePhaseService.phases().then(function (phaseMap) {
          angular.forEach(phaseMap, function (array, phase) {
              //  Would be nice to use more caches but can't get keys
              cache.put(phase, {});
              phases.push(phases);
            }
          );
          cache.put('All', allCache);
          phases.push('All');
        }, function () {
          $location.path('/error');
        });
      }

      initializeCache();

      return {
        clearPhase: function (phase) {
          cache.put(phase, {});
        },

        clearAll: function () {
          phases.forEach(function (phase) {
            cache.put(phase, {});
          });
        },

        initializeGames: function (games) {
          this.clearAll();
          games.forEach(function (game) {
            cache.get(game.gamePhase).put(game.id, game);
            allCache.put(game.id, game);
          });
          //  TODO - publish?
        },

        putUpdatedGame: function (game) {
          var phaseCache = cache.get(game.gamePhase);
          var existingGame = allCache[game.id];
          if (angular.isDefined(existingGame)) {
            if (game.lastUpdate <= existingGame.lastUpdate) {
              console.warn('Skipping Stale game update for ' + game.id);
              return;
            }
            //  TODO publish?
          }
          phaseCache[game.id] = game;
          allCache[game.id] = game;
        },

        getAllForPhase: function (phase) {
          return cache.get(phase);
        }
      };
    }]);
