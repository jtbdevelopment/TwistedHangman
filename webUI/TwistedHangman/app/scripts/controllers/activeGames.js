'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
  .controller('ActiveGames', function ($scope, $http) {
    //  TODO load from rest
    $scope.activeGames = [
      {
        maskedForPlayerMD5: '1',
        id: 'ID1',
        created: 1416359122,
        lastUpdate: 1416359122,
        gamePhase: 'Playing',
        players: {'1': 'X', '2': 'Y', '3': 'Z'},
        features: ['SystemPuzzles', 'ThreePlus', 'Thieving'],
        solverStates: {
          '1': {
            workingWordPhrase: '___ __T ___',
            category: 'phrase',
            penalties: 1,
            penaltiesRemaining: 5,
            moveCount: 2,
            maxPenalties: 6,
            features: ['SystemPuzzles', 'ThreePlus', 'Thieving', 'ThievingCountTracking', 'ThievingPositionTracking'],
            featureData: {
              ThievingPositionTracking: [false, false, false, false, false, false, false, false, false, false, false],
              ThievingCountTracking: 0
            },
            badlyGuessedLetters: ['W'],
            guessedLetters: ['T', 'W'],
            isGameWon: false,
            isGameLost: false,
            isGameOver: false
          }
        },
        featureData: {},
        playerStates: {'1': 'Accepted', '2': 'Accepted', '3': 'Accepted'},
        wordPhraseSetter: '0',
        playerScores: {'1': 0, '2': -1, '3': 4}
      },
      {
        maskedForPlayerMD5: '1',
        id: 'ID2',
        created: 1416359880,
        lastUpdate: 1416359884,
        gamePhase: 'Playing',
        players: {'1': 'X', '2': 'Y'},
        features: ['SystemPuzzles', 'TwoPlayer', 'Thieving'],
        solverStates: {
          '1': {
            workingWordPhrase: '___ ___ D__',
            category: 'phrase',
            penalties: 1,
            penaltiesRemaining: 5,
            moveCount: 2,
            maxPenalties: 6,
            features: ['SystemPuzzles', 'ThreePlus', 'Thieving', 'ThievingCountTracking', 'ThievingPositionTracking'],
            featureData: {
              ThievingPositionTracking: [false, false, false, false, false, false, false, false, true, false, false],
              ThievingCountTracking: 1
            },
            badlyGuessedLetters: ['W'],
            guessedLetters: ['W'],
            isGameWon: false,
            isGameLost: false,
            isGameOver: false
          }
        },
        featureData: {},
        playerStates: {'1': 'Accepted', '2': 'Accepted'},
        wordPhraseSetter: '0',
        playerScores: {'1': 0, '2': -1}
      },
      {
        maskedForPlayerMD5: '1',
        id: 'ID1',
        created: 1416359122,
        lastUpdate: 1416359122,
        gamePhase: 'Playing',
        players: {'1': 'X'},
        features: ['SystemPuzzles', 'SinglePlayer', 'Thieving'],
        solverStates: {
          '1': {
            workingWordPhrase: '___ __T ___',
            category: 'phrase',
            penalties: 1,
            penaltiesRemaining: 5,
            moveCount: 2,
            maxPenalties: 6,
            features: ['SystemPuzzles', 'ThreePlus', 'Thieving', 'ThievingCountTracking', 'ThievingPositionTracking'],
            featureData: {
              ThievingPositionTracking: [false, false, false, false, false, false, false, false, false, false, false],
              ThievingCountTracking: 0
            },
            badlyGuessedLetters: ['W'],
            guessedLetters: ['T', 'W'],
            isGameWon: false,
            isGameLost: false,
            isGameOver: false
          }
        },
        featureData: {},
        playerStates: {'1': 'Accepted'},
        wordPhraseSetter: '0',
        playerScores: {'1': 0}
      },
    ];

  });
