// Some dummy code to play with atomsphere sockets

$(function () {
  "use strict";
  var socket = $.atmosphere;
  var request = {
    url: document.location.toString().replace('#/', '') + 'livefeed',
    contentType: "application/json",
    logLevel: 'debug',
    transport: 'websocket',
    trackMessageLength: true,
    fallbackTransport: 'long-polling'
  };


  request.onOpen = function (response) {
    console.error('onOpen: ' + JSON.stringify(response));
    console.error('Atmosphere connected using ' + response.transport);
  };

  request.onMessage = function (response) {
    console.error('onMessage: ' + JSON.stringify(response));
  };

  request.onClose = function (response) {
    console.error('onClose: ' + JSON.stringify(response));
  };

  request.onError = function (response) {
    console.error('onError: ' + JSON.stringify(response));
  };

  console.error('Subscribing: ' + JSON.stringify(request));
  var subSocket = socket.subscribe(request);

});
