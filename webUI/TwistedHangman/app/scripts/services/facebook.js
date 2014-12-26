/*global FB:false */
'use strict';

angular.module('twistedHangmanApp').factory('twFacebook',
  ['$http', '$location',
    function ($http, $location) {
      var loaded = false;
      var facebookAppId = '';

      //  TODO - should probably use promises not callbacks
      function loadFB(callback) {
        if (!loaded) {
          $http.get('/api/social/apis', {cache: true}).success(function (response) {
            facebookAppId = response.facebookAppId;
            window.fbAsyncInit = function () {
              FB.init({
                appId: facebookAppId,
                xfbml: true,
                version: 'v2.1'
              });
              callback();
            };

            (function (d, s, id) {
              var js, fjs = d.getElementsByTagName(s)[0];
              if (d.getElementById(id)) {
                return;
              }
              js = d.createElement(s);
              js.id = id;
              js.src = '//connect.facebook.net/en_US/sdk.js';
              fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));

            loaded = true;
          }).error(function () {
            $location.path('/error');
          });
        } else {
          callback();
        }
      }

      return {
        canAutoSignIn: function (callback) {
          loadFB(function () {
            FB.getLoginStatus(function (response) {
              callback(response.status === 'connected');
            });
            callback(false);
          });
        },
        playerAndFBMatch: function (player, callback) {
          loadFB(function () {
            if (player.source === 'facebook') {
              FB.getLoginStatus(function (response) {
                if (response.status === 'connected') {
                  callback(response.authResponse.userID === player.sourceId);
                }
                else {
                  callback(false);
                }
              });
            } else {
              callback(false);
            }
          });
        }
      };
    }
  ]);
