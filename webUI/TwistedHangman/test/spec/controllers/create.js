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
    expect(scope.submitEnabled).toBe(true);
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
    expect(scope.submitEnabled).toBe(true);
  });

  it('changes to two player from multiplayer', function () {
    //  should not change
    scope.thieving = '';
    scope.drawGallows = 'DrawGallows';
    scope.drawFace = 'DrawFace';
    scope.gamePace = 'TurnBased';
    scope.puzzleSetter = '';
    scope.winners = '';

    //  should change
    scope.playerCount = 'X';
    scope.players = ['1', '2'];
    scope.h2hEnabled = false;
    scope.alternatingEnabled = false;
    scope.alternatingEnabled = false;
    scope.turnBasedEnabled = false;

    scope.setTwoPlayers();

    expect(scope.url).toEqual('/api/player/MANUAL1/new');
    expect(scope.thieving).toEqual('');
    expect(scope.drawGallows).toEqual('DrawGallows');
    expect(scope.drawFace).toEqual('DrawFace');
    expect(scope.gamePace).toEqual('TurnBased');

    expect(scope.playerCount).toEqual('TwoPlayer');
    expect(scope.players).toEqual([]);
    expect(scope.gamePace).toEqual('TurnBased');
    expect(scope.puzzleSetter).toEqual('');
    expect(scope.winners).toEqual('');
    expect(scope.h2hEnabled).toBe(true);
    expect(scope.alternatingEnabled).toBe(true);
    expect(scope.allFinishedEnabled).toBe(true);
    expect(scope.turnBasedEnabled).toBe(true);
    expect(scope.submitEnabled).toBe(false);
  });

  it('changes to two player from multiplayer with only one opponent', function () {
    scope.players = ['2'];
    scope.setTwoPlayers();
    expect(scope.players).toEqual(['2']);
    expect(scope.submitEnabled).toBe(true);
  });

  it('changes to multi player from multiplayer', function () {
    //  should not change
    scope.thieving = '';
    scope.drawGallows = 'DrawGallows';
    scope.drawFace = 'DrawFace';
    scope.gamePace = 'TurnBased';
    scope.winners = '';
    scope.players = ['1', '2'];

    //  should change
    scope.puzzleSetter = '';
    scope.playerCount = 'X';
    scope.h2hEnabled = true;
    scope.alternatingEnabled = false;
    scope.alternatingEnabled = false;
    scope.turnBasedEnabled = false;

    scope.setThreePlayers();

    expect(scope.url).toEqual('/api/player/MANUAL1/new');
    expect(scope.thieving).toEqual('');
    expect(scope.drawGallows).toEqual('DrawGallows');
    expect(scope.drawFace).toEqual('DrawFace');
    expect(scope.gamePace).toEqual('TurnBased');

    expect(scope.playerCount).toEqual('ThreePlus');
    expect(scope.players).toEqual(['1', '2']);
    expect(scope.gamePace).toEqual('TurnBased');
    expect(scope.puzzleSetter).toEqual('SystemPuzzles');
    expect(scope.winners).toEqual('');
    expect(scope.h2hEnabled).toBe(false);
    expect(scope.alternatingEnabled).toBe(true);
    expect(scope.allFinishedEnabled).toBe(true);
    expect(scope.turnBasedEnabled).toBe(true);
    expect(scope.submitEnabled).toBe(true);
  });

  it('test submit enable for single player game', function () {
    scope.setSinglePlayer();
    expect(scope.players).toEqual([]);
    expect(scope.submitEnabled).toBe(true);

    scope.players = ['1'];
    scope.calcSubmitEnabled();
    expect(scope.players).toEqual(['1']);
    expect(scope.submitEnabled).toBe(false);
  });


  it('test submit enable for two player game', function () {
    scope.setTwoPlayers();
    expect(scope.players).toEqual([]);
    expect(scope.submitEnabled).toBe(false);

    scope.players = ['1'];
    scope.calcSubmitEnabled();
    expect(scope.players).toEqual(['1']);
    expect(scope.submitEnabled).toBe(true);
  });

  it('test submit enable for multi player game', function () {
    scope.setThreePlayers();
    expect(scope.players).toEqual([]);
    expect(scope.submitEnabled).toBe(false);

    scope.players = ['1'];
    scope.calcSubmitEnabled();
    expect(scope.players).toEqual(['1']);
    expect(scope.submitEnabled).toBe(false);

    scope.players = ['1', '2'];
    scope.calcSubmitEnabled();
    expect(scope.players).toEqual(['1', '2']);
    expect(scope.submitEnabled).toBe(true);

    scope.players = ['1', '2', '3'];
    scope.calcSubmitEnabled();
    expect(scope.players).toEqual(['1', '2', '3']);
    expect(scope.submitEnabled).toBe(true);
  });
});

