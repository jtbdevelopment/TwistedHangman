'use strict';

angular.module('twistedHangmanApp').factory('twGameCache',
  ['$rootScope', '$cacheFactory', '$location', '$q', '$http', 'twGamePhaseService', 'twCurrentPlayerService',
    function ($rootScope, $cacheFactory, $location, $q, $http, twGamePhaseService, twCurrentPlayerService) {
      var ALL = 'All';
      var gameCache = $cacheFactory('game-gameCache');
      var phases = [];
      var loadedCounter = 0;

      function initializeSubCaches() {
        phases.forEach(function (phase) {
            var phaseCache = gameCache.get(phase);
            if (angular.isDefined(phaseCache)) {
              var idMap = phaseCache.idMap;
              if (angular.isDefined(idMap)) {
                Object.keys(phaseCache.idMap).forEach(function (key) {
                  delete phaseCache.idMap[key];
                });
                phaseCache.games.splice(0);
              }
            } else {
              //  Would be nice to use more caches but can't get keys
              gameCache.put(phase, {
                idMap: {},
                games: []
              });
            }
          }
        );
      }

      function initializeCache() {
        initializeSubCaches();
        $http.get(twCurrentPlayerService.currentPlayerBaseURL() + '/games').success(function (data) {
          data.forEach(function (game) {
            cache.putUpdatedGame(game);
          });
          ++loadedCounter;
          $rootScope.$broadcast('gameCachesLoaded', loadedCounter);
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
          var phaseIndex = phaseCache.idMap[game.id];

          var allCache = gameCache.get(ALL);
          var allIndex = allCache.idMap[game.id];

          //  TODO publish?
          if (angular.isDefined(allIndex)) {
            var existingGame = allCache.games[allIndex];
            if (game.lastUpdate <= existingGame.lastUpdate) {
              console.warn('Skipping Stale game update for ' + game.id);
              return;
            }
            allCache.games[allIndex] = game;
            phaseCache.games[phaseIndex] = game;
          } else {
            phaseCache.games.push(game);
            phaseCache.idMap[game.id] = phaseCache.games.indexOf(game);
            allCache.games.push(game);
            allCache.idMap[game.id] = allCache.games.indexOf(game);
          }
        },

        getGamesForPhase: function (phase) {
          return gameCache.get(phase).games;
        }
        /*  If debugging needed
         ,getMapForPhase: function (phase) {
         return gameCache.get(phase).idMap;
        }
         */
      };

      $rootScope.$on('playerSwitch', function () {
        initializeCache();
      });

      $rootScope.$on('refreshGames', function () {
        initializeCache();
      });

      initialize();

      return cache;
    }]);
