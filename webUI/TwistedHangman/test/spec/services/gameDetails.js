'use strict';

describe('Service: twGameDetails', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var game = {
    id: 'id',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
    wordPhraseSetter: 'md4',
    solverStates: {
      md1: {isPuzzleOver: true, isPuzzleSolved: true, field: 'X'},
      md2: {isPuzzleOver: true, isPuzzleSolved: false, field: 'Y'},
      md3: {isPuzzleOver: false, isPuzzleSolved: false, field: 'Z'}
    },
    playerRunningScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5},
    playerRoundScores: {'md1': 1, 'md2': 0, 'md3': -1, 'md4': 3, 'md5': 2}
  };

  var undef;
  var service;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($injector) {
    service = $injector.get('twGameDetails');
  }));


  describe('test various state computation functions', function () {

    it('role for player', function () {
      expect(service.roleForPlayer(game, 'md4')).toEqual('Set Puzzle');
      ['md1', 'md2', 'md3', 'md5'].forEach(function (md) {
        expect(service.roleForPlayer(game, md)).toEqual('Solver');
      });
    });

    it('role for player with undefined game', function () {
      expect(service.roleForPlayer(undef, 'md4')).toEqual('');
    });

    it('gameEndForPlayer for player', function () {
      expect(service.gameEndForPlayer(game, 'md1')).toEqual('Solved!');
      expect(service.gameEndForPlayer(game, 'md2')).toEqual('Hung!');
      expect(service.gameEndForPlayer(game, 'md3')).toEqual('Not Solved.');
      expect(service.gameEndForPlayer(game, 'md4')).toEqual('N/A');
      expect(service.gameEndForPlayer(game, 'md5')).toEqual('Unknown');
    });

    it('gameEndForPlayer with undefined game', function () {
      expect(service.gameEndForPlayer(undef, 'md4')).toEqual('');
    });

    it('stateForPlayer for player', function () {
      expect(service.stateForPlayer(game, 'md1', 'field')).toEqual('X');
      expect(service.stateForPlayer(game, 'md2', 'field')).toEqual('Y');
      expect(service.stateForPlayer(game, 'md3', 'field')).toEqual('Z');
      expect(service.stateForPlayer(game, 'md4', 'field')).toEqual('N/A');
      expect(service.stateForPlayer(game, 'md5', 'field')).toEqual('Unknown');
    });

    it('stateForPlayer with undefined game', function () {
      expect(service.stateForPlayer(undef, 'md4')).toEqual('');
    });

    it('gameScoreForPlayer with undefined game', function () {
      expect(service.gameScoreForPlayer(undef, 'md4')).toEqual('');
    });

    it('gameScoreForPlayer for player', function () {
      angular.forEach({'md1': 1, 'md2': 0, 'md3': -1, 'md4': 3, 'md5': 2}, function (value, key) {
        expect(service.gameScoreForPlayer(game, key)).toEqual(value);
      });
    });

    it('runningScoreForPlayer with undefined game', function () {
      expect(service.runningScoreForPlayer(undef, 'md4')).toEqual('');
    });

    it('runningScoreForPlayer for player', function () {
      angular.forEach({'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5}, function (value, key) {
        expect(service.runningScoreForPlayer(game, key)).toEqual(value);
      });
    });
  });

});
