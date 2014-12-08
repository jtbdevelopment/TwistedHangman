'use strict';

angular.module('twistedHangmanApp').factory('twLiveGameFeed',
  ['$rootScope', '$location', '$http', 'twGamePhaseService', 'twCurrentPlayerService',
    function ($rootScope, $location, $http, twGamePhaseService, twCurrentPlayerService) {
      var request = {
        url: '',
        contentType: 'application/json',
        logLevel: 'debug',
        transport: 'websocket',
        trackMessageLength: true,
        fallbackTransport: 'long-polling',

        onOpen: function (response) {
          console.info(this.url + 'Atmosphere connected using ' + response.transport);
        },

        onMessage: function (response) {
          if (angular.isDefined(response.messages)) {
            response.messages.forEach(function (message) {
              if (angular.isDefined(message.messageType)) {
                switch (message.messageType) {
                  case 'game':
                    console.error('got a game ' + message);
                    return;
                }
                console.warn('onMessage: unknown message type \'' + message.messageType + '\'');
              }
              console.warn('unknown message structure ' + message);
            });
          } else {
            console.error(this.url + ' unknown onMessage: ' + JSON.stringify(response));
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
        request.url = '/livefeed/' + twCurrentPlayerService.currentID();
        subscribed = socket.subscribe(request);
      }

      subscribeToCurrentPlayer();

      $rootScope.$on('playerSwitch', function () {
        if (angular.isDefined(subscribed)) {
          subscribed.close();
        }
        var x;
        subscribed = x;
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
