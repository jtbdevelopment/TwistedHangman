'use strict';

//  TODO - test
angular.module('twistedHangmanApp').directive('twistedGameButton', function () {
  return {
    restrict: 'AE',
    templateUrl: 'views/gamebuttons.html',
    replace: true
  };
});
