'use strict';

//  TODO - do we need to publish timing events on various loads so that there are not race conditions
//  i.e. someone trying to use cache before initalized
angular.module('twistedHangmanApp').factory('twGameCache',
  ['$rootScope', '$cacheFactory', '$location', '$q', '$http', 'twGamePhaseService', 'twCurrentPlayerService',
    function ($rootScope, $cacheFactory, $location, $q, $http, twGamePhaseService, twCurrentPlayerService) {
      var ALL = 'All';
      var gameCache = $cacheFactory('game-gameCache');
      var phases = [];

      function initializeGameCache() {
        gameCache.removeAll();
        gameCache.put(ALL, {});
        phases.forEach(function (phase) {
            //  Would be nice to use more caches but can't get keys
            gameCache.put(phase, {});
            phases.push(phase);
          }
        );
      }

      function initializeCache() {
        initializeGameCache();
        $http.get(twCurrentPlayerService.currentPlayerBaseURL() + '/games').success(function (data) {
          data.forEach(function (game) {
            cache.putUpdatedGame(game);
          });
        }).error(function () {
          $location.path('/error');
        });
      }

      //  TODO - should we store gameCache locally and initialize from gameCache?  Then pull updates?
      //  If we do - make cache player id specific
      function initialize() {
        twGamePhaseService.phases().then(function (phaseMap) {
          phases = [ALL];
          angular.forEach(phaseMap, function (array, phase) {
            phases.push(phase);
          });
          initializeCache();
        }, function () {
          $location.path('/error');
        });
      }

      var cache = {
        putUpdatedGame: function (game) {
          var phaseCache = gameCache.get(game.gamePhase);
          var existingGame = gameCache.get(ALL)[game.id];
          if (angular.isDefined(existingGame)) {
            if (game.lastUpdate <= existingGame.lastUpdate) {
              console.warn('Skipping Stale game update for ' + game.id);
              return;
            }
            //  TODO publish?
          }
          phaseCache[game.id] = game;
          gameCache.get(ALL)[game.id] = game;
        },

        getAllForPhase: function (phase) {
          return gameCache.get(phase);
        }
      };

      initialize();

      $rootScope.$on('playerSwitch', function () {
        initializeCache();
      });

      return cache;
    }]);
