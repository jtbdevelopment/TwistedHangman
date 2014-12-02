'use strict';

//  TODO - test
angular.module('twistedHangmanApp').directive('twistedGameButton', function () {
  return {
    restrict: 'AE',
    templateUrl: 'views/gameButtons.html',
    replace: true
  };
});
