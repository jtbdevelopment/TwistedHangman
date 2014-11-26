'use strict';

var sharedScopeGameSummary = {
  game: {
    id: 'id2',
    players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
    puzzleSetter: 'md4',
    solverStates: {
      md1: {isGameOver: true, isGameWon: true},
      md2: {isGameOver: true, isGameWon: false},
      md3: {isGameOver: false, isGameWon: false}
    },
    playerScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5}
  },
  gameID: 'id'
};
describe('Controller: GameSummary', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $controller) {
    scope = $rootScope.$new();

    var mockShowGameCache = {
      get: function (key) {
        if (key === 'scope') {
          return sharedScopeGameSummary;
        }
        return null;
      }
    };

    ctrl = $controller('GameSummaryCtrl', {
      $scope: scope,
      twShowGameCache: mockShowGameCache
    });
  }));

  it('initializes', function () {
    expect(scope.sharedScope).toEqual(sharedScopeGameSummary);
  });

  it('role for player', function () {
    expect(scope.roleForPlayer('md4')).toEqual('Set Puzzle');
    ['md1', 'md2', 'md3', 'md5'].forEach(function (md) {
      expect(scope.roleForPlayer(md)).toEqual('Solver');
    });
  });
  it('gameEndForPlayer for player', function () {
    expect(scope.gameEndForPlayer('md1')).toEqual('Solved');
    expect(scope.gameEndForPlayer('md2')).toEqual('Hung');
    expect(scope.gameEndForPlayer('md3')).toEqual('Incomplete');
    expect(scope.gameEndForPlayer('md4')).toEqual('N/A');
    expect(scope.gameEndForPlayer('md5')).toEqual('Unknown');
  });
  it('gameScoreForPlayer for player', function () {
    ['md1', 'md2', 'md3', 'md4', 'md5'].forEach(function (md) {
      expect(scope.gameScoreForPlayer(md)).toEqual(0);
    });
  });
  it('runningScoreForPlayer for player', function () {

  });
});
