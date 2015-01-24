/*global $:false */
'use strict';
angular.module('twistedHangmanApp').factory('twLiveGameFeed',
  ['$rootScope', 'twPlayerService',
    function ($rootScope, twPlayerService) {
      var request = {
        url: '',
        contentType: 'application/json',
        logLevel: 'debug',
        //  AWS doesn't support so kind of pointless and slow to default to it
        //transport: 'websocket',
        transport: 'long-polling',
        trackMessageLength: true,
        fallbackTransport: 'long-polling',

        onOpen: function (response) {
          console.info(this.url + ' Atmosphere connected using ' + response.transport);
          $rootScope.$broadcast('liveFeedEstablished');
        },

        onMessage: function (response) {
          if (angular.isDefined(response.messages)) {
            response.messages.forEach(function (messageString) {
              var message;
              try {
                message = JSON.parse(messageString);
              } catch (error) {
                console.error('got non-parseable message');
                return;
              }

              if (angular.isDefined(message.messageType)) {
                switch (message.messageType.toString()) {
                  case 'Game':
                    $rootScope.$broadcast('gameUpdate', message.game.id, message.game);
                    return;
                  case 'Heartbeat':
                    console.info('got a heartbeat ' + JSON.stringify(message.message));
                    return;
                  default:
                    console.warn('onMessage: unknown message type \'' + message.messageType + '\'');
                    break;
                }
                console.warn('onMessage: unknown message type \'' + message.messageType + '\'');
              }
              console.warn('unknown message structure ' + message);
            });
          } else {
            console.warn(this.url + ' unknown onMessage: ' + JSON.stringify(response));
          }
        },

        onClose: function (response) {
          console.warn(this.url + ' closed: ' + JSON.stringify(response));
        },

        onError: function (response) {
          console.error(this.url + ' onError: ' + JSON.stringify(response));
        }
      };

      var socket = $.atmosphere;
      var subscribed;

      function subscribeToCurrentPlayer() {
        request.url = '/livefeed/' + twPlayerService.currentID();
        subscribed = socket.subscribe(request);
      }

      $rootScope.$on('playerLoaded', function () {
        if (angular.isDefined(subscribed)) {
          subscribed.close();
        }
        subscribed = undefined;
        subscribeToCurrentPlayer();
      });

      return {
        handler: function () {
          return request;
        }
      };
    }

  ]
);
