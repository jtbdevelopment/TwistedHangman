'use strict';


angular.module('twistedHangmanApp').factory('twPlayerService',
  ['$http', '$rootScope', '$location', '$window', 'twFacebook',
    function ($http, $rootScope, $location, $window, twFacebook) {
      var realPID = '';
      var simulatedPID = '';
      var BASE_PLAYER_URL = '/api/player';
      var FRIENDS_PATH = '/friends';

      var simulatedPlayer;

      var service = {
        overridePID: function (newpid) {
          $http.put(this.currentPlayerBaseURL() + '/admin/' + newpid).success(function (data) {
            simulatedPID = data.id;
            simulatedPlayer = data;
            broadcastLoaded();
          }).error(function () {
            $location.path('/error');
          });
        },
        realPID: function () {
          return realPID;
        },


        currentID: function () {
          return simulatedPID;
        },
        currentPlayerBaseURL: function () {
          return BASE_PLAYER_URL;
        },
        currentPlayerFriends: function () {
          return $http.get(this.currentPlayerBaseURL() + FRIENDS_PATH).then(function (response) {
            console.info(JSON.stringify(response.data));
            return response.data;
          });
        },
        currentPlayer: function () {
          return simulatedPlayer;
        }
      };

      function broadcastLoaded() {
        $rootScope.$broadcast('playerLoaded');
      }

      function signOutAndRedirect() {
        $http.post('/signout').success(function () {
          $window.location = '/';
        }).error(function () {
          $location.path('/error');
        });
      }

      function initializePlayer() {
        $http.get('/api/security', {cache: true}).success(function (response) {
          simulatedPlayer = response;
          realPID = simulatedPlayer.id;
          simulatedPID = simulatedPlayer.id;
          switch (simulatedPlayer.source) {
            case 'facebook':
              twFacebook.playerAndFBMatch(simulatedPlayer).then(function (match) {
                if (!match) {
                  signOutAndRedirect();
                } else {
                  broadcastLoaded();
                }
              }, function () {
                signOutAndRedirect();
              });
              break;
            default:
              broadcastLoaded();
              break;
          }
        }).error(function () {
          $location.path('/error');
        });
      }

      initializePlayer();

      return service;
    }]);

