'use strict';

var SCOPE = 'scope';
var LETTERA = 'A'.charCodeAt(0);

//  TODO - testing
angular.module('twistedHangmanApp').factory('twShowGameService', function ($rootScope, twShowGameCache) {
  function computeWordPhrase(sharedScope) {
    sharedScope.workingWordPhraseArray = sharedScope.gameState.workingWordPhrase.split('');
    for (var i = 0; i < sharedScope.workingWordPhraseArray.length; i++) {
      var r = 'regularwp';
      if (typeof sharedScope.gameState.featureData.ThievingPositionTracking !== 'undefined') {
        r = (sharedScope.gameState.featureData.ThievingPositionTracking[i] === true) ? 'stolenwp' : 'stealablewp';
      }
      sharedScope.workingWordPhraseClasses[i] = r;
    }
  }

  function computeImage(sharedScope) {
    if (sharedScope.gameState.penalties === sharedScope.gameState.maxPenalties) {
      sharedScope.image = 'hangman13.png';
      return;
    }
    var n = 0;
    switch (sharedScope.gameState.maxPenalties) {
      case 6:
      case 10:
        n = sharedScope.gameState.penalties + 3;
        break;
      default:
        n = sharedScope.gameState.penalties;
        break;
    }
    sharedScope.image = 'hangman' + n + '.png';
  }

  function computeKeyboard(sharedScope) {
    //  TODO - show stolen letters when stealing auto gets the rest
    sharedScope.gameState.guessedLetters.forEach(function (item) {
      sharedScope.letterClasses[item.charCodeAt(0) - LETTERA] = 'guessedkb';
    });
    sharedScope.gameState.badlyGuessedLetters.forEach(function (item) {
      sharedScope.letterClasses[item.charCodeAt(0) - LETTERA] = 'badguesskb';
    });
  }

  return {
    initializeScope: function ($scope) {
      twShowGameCache.put(SCOPE, $scope);
      $scope.workingWordPhraseArray = [];
      $scope.workingWordPhraseClasses = [];
      $scope.letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
      $scope.letterClasses = [];
      $scope.letters.forEach(function () {
        $scope.letterClasses.push('regular');
      });
    },
    computeGameState: function () {
      var sharedScope = twShowGameCache.get(SCOPE);
      if (typeof sharedScope.player === 'undefined' || typeof sharedScope.game === 'undefined') {
        return;
      }
      sharedScope.gameState = sharedScope.game.solverStates[sharedScope.player.md5];
      computeImage(sharedScope);
      computeWordPhrase(sharedScope);
      computeKeyboard(sharedScope);
    },

    processGame: function (data) {
      var sharedScope = twShowGameCache.get(SCOPE);
      sharedScope.game = data;
      sharedScope.lastUpdate = new Date(sharedScope.game.lastUpdate * 1000);
      sharedScope.created = new Date(sharedScope.game.created * 1000);
      this.computeGameState();
    },

    processUpdate: function (data) {
      var sharedScope = twShowGameCache.get(SCOPE);
      var beforePhase = sharedScope.game.gamePhase;
      this.processGame(data);
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      if (data.gamePhase !== beforePhase) {
        $rootScope.$broadcast('refreshGames', beforePhase);
      }
      //  TODO
      console.log(data);
    }
  };

});

