'use strict';

angular.module('twistedHangmanApp').directive('twistedGameButton', function () {
  return {
    restrict: 'AE',
    templateUrl: 'views/gamebuttons.html',
    replace: true
  };
});
