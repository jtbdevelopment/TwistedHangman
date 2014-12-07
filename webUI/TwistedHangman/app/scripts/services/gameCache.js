'use strict';

angular.module('twistedHangmanApp').factory('twGameCache',
  ['$rootScope', '$cacheFactory', '$location', '$http', 'twGamePhaseService', 'twCurrentPlayerService',
    function ($rootScope, $cacheFactory, $location, $http, twGamePhaseService, twCurrentPlayerService) {
      var ALL = 'All';
      var gameCache = $cacheFactory('game-gameCache');
      var phases = [];
      var loadedCounter = 0;

      function initializeSubCaches() {
        phases.forEach(function (phase) {
            var phaseCache = gameCache.get(phase);
            if (angular.isDefined(phaseCache)) {
              Object.keys(phaseCache.idMap).forEach(function (key) {
                delete phaseCache.idMap[key];
              });
              phaseCache.games.splice(0);
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
          phases.slice(0);
          phases.push(ALL);
          angular.forEach(phaseMap, function (array, phase) {
            phases.push(phase);
          });
          initializeCache();
        }, function () {
          $location.path('/error');
        });
      }

      var cache = {
        putUpdatedGame: function (updatedGame) {
          var allCache = gameCache.get(ALL);
          var allIndex = allCache.idMap[updatedGame.id];

          //  TODO publish?
          if (angular.isDefined(allIndex)) {
            var existingGame = allCache.games[allIndex];
            if (updatedGame.lastUpdate <= existingGame.lastUpdate) {
              console.warn('Skipping Stale game update for ' + updatedGame.id);
              return;
            }
            allCache.games[allIndex] = updatedGame;

            var existingPhaseCache = gameCache.get(existingGame.gamePhase);
            var existingPhaseIndex = existingPhaseCache.idMap[existingGame.id];
            if (existingGame.gamePhase === updatedGame.gamePhase) {
              existingPhaseCache.games[existingPhaseIndex] = updatedGame;
            } else {
              var newPhaseCache = gameCache.get(updatedGame.gamePhase);
              newPhaseCache.games.push(updatedGame);
              newPhaseCache.idMap[updatedGame.id] = newPhaseCache.games.indexOf(updatedGame);
              existingPhaseCache.games.splice(existingPhaseIndex, 1);
              delete existingPhaseCache.idMap[existingGame.id];
            }

          } else {
            var phaseCache = gameCache.get(updatedGame.gamePhase);
            phaseCache.games.push(updatedGame);
            phaseCache.idMap[updatedGame.id] = phaseCache.games.indexOf(updatedGame);
            allCache.games.push(updatedGame);
            allCache.idMap[updatedGame.id] = allCache.games.indexOf(updatedGame);
          }
        },

        getGameForID: function (id) {
          var allCache = gameCache.get(ALL);
          if (angular.isDefined(allCache)) {
            var index = allCache.idMap[id];
            if (angular.isDefined(index)) {
              return allCache.games[index];
            }
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
