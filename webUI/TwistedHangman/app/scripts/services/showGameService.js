'use strict';

var LETTERA = 'A'.charCodeAt(0);

angular.module('twistedHangmanApp').factory('twShowGameService', function ($rootScope) {
  function computeWordPhrase(scope) {
    scope.workingWordPhraseArray = scope.gameState.workingWordPhrase.split('');
    for (var i = 0; i < scope.workingWordPhraseArray.length; i++) {
      var r = 'regularwp';
      if (typeof scope.gameState.featureData.ThievingPositionTracking !== 'undefined') {
        r = (scope.gameState.featureData.ThievingPositionTracking[i] === true) ? 'stolenwp' : 'stealablewp';
      }
      scope.workingWordPhraseClasses[i] = r;
    }
  }

  function computeImage(scope) {
    if (scope.gameState.penalties === scope.gameState.maxPenalties) {
      scope.image = 'hangman13.png';
      return;
    }
    var n = 0;
    switch (scope.gameState.maxPenalties) {
      case 6:
      case 10:
        n = scope.gameState.penalties + 3;
        break;
      default:
        n = scope.gameState.penalties;
        break;
    }
    scope.image = 'hangman' + n + '.png';
  }

  function computeKeyboard(scope) {
    //  TODO - show stolen letters when stealing auto gets the rest
    scope.gameState.guessedLetters.forEach(function (item) {
      scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'guessedkb';
    });
    scope.gameState.badlyGuessedLetters.forEach(function (item) {
      scope.letterClasses[item.charCodeAt(0) - LETTERA] = 'badguesskb';
    });
  }

  return {
    initializeScope: function (scope) {
      scope.workingWordPhraseArray = [];
      scope.workingWordPhraseClasses = [];
      scope.letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
      scope.letterClasses = [];
      scope.letters.forEach(function () {
        scope.letterClasses.push('regular');
      });
    },
    computeGameState: function (scope) {
      if (typeof scope.player === 'undefined' || typeof scope.game === 'undefined') {
        return;
      }
      scope.gameState = scope.game.solverStates[scope.player.md5];
      computeImage(scope);
      computeWordPhrase(scope);
      computeKeyboard(scope);
    },

    processGame: function (scope, data) {
      scope.game = data;
      //  TODO - convert to millis on server
      scope.lastUpdate = new Date(scope.game.lastUpdate * 1000);
      scope.created = new Date(scope.game.created * 1000);
      this.computeGameState(scope);
    },

    processUpdate: function (scope, data) {
      var beforePhase = scope.game.gamePhase;
      this.processGame(scope, data);
      $rootScope.$broadcast('refreshGames', data.gamePhase);
      if (data.gamePhase !== beforePhase) {
        $rootScope.$broadcast('refreshGames', beforePhase);
      }
    }
  };

});

