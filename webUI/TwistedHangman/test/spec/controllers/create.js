'use strict';

describe('Controller: CreateCtrl', function () {
  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, http;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $httpBackend) {
    scope = $rootScope.$new();
    http = $httpBackend;
    ctrl = $controller('CreateCtrl', {
      $scope: scope
    });
  }));

  it('initializes', function () {
    expect(scope.url).toEqual('/api/player/MANUAL1/new');
    expect(scope.thieving).toEqual('Thieving');
    expect(scope.drawGallows).toEqual('');
    expect(scope.drawFace).toEqual('');
    expect(scope.gamePace).toEqual('');

    expect(scope.playerCount).toEqual('SinglePlayer');
    expect(scope.players).toEqual([]);
    expect(scope.gamePace).toEqual('');
    expect(scope.puzzleSetter).toEqual('SystemPuzzles');
    expect(scope.winners).toEqual('SingleWinner');
    expect(scope.h2hEnabled).toBe(false);
    expect(scope.alternatingEnabled).toBe(false);
    expect(scope.allFinishedEnabled).toBe(false);
    expect(scope.turnBasedEnabled).toBe(false);
  });

  it('resets to single player', function () {
    //  should not change
    scope.thieving = '';
    scope.drawGallows = 'DrawGallows';
    scope.drawFace = 'DrawFace';

    //  should change
    scope.playerCount = 'X';
    scope.gamePace = 'TurnBased';
    scope.players = ['1'];
    scope.puzzleSetter = '';
    scope.winners = '';
    scope.h2hEnabled = true;
    scope.alternatingEnabled = true;
    scope.alternatingEnabled = true;
    scope.turnBasedEnabled = true;

    scope.setSinglePlayer();

    expect(scope.url).toEqual('/api/player/MANUAL1/new');
    expect(scope.thieving).toEqual('');
    expect(scope.drawGallows).toEqual('DrawGallows');
    expect(scope.drawFace).toEqual('DrawFace');
    expect(scope.gamePace).toEqual('');

    expect(scope.playerCount).toEqual('SinglePlayer');
    expect(scope.players).toEqual([]);
    expect(scope.gamePace).toEqual('');
    expect(scope.puzzleSetter).toEqual('SystemPuzzles');
    expect(scope.winners).toEqual('SingleWinner');
    expect(scope.h2hEnabled).toBe(false);
    expect(scope.alternatingEnabled).toBe(false);
    expect(scope.allFinishedEnabled).toBe(false);
    expect(scope.turnBasedEnabled).toBe(false);
  });
});

