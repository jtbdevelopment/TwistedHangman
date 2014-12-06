'use strict';

describe('Controller: CreateCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, http, q, rootScope, featureDeferred, location, friendsDeferred, gameCache;
  var friends = {md1: 'friend1', md2: 'friend2', md3: 'friend3', md4: 'friend4'};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    gameCache = {putUpdatedGame: jasmine.createSpy()};
    location = {path: jasmine.createSpy()};
    scope = rootScope.$new();
    var mockFeatureService = {
      features: function () {
        featureDeferred = q.defer();
        return featureDeferred.promise;
      }
    };

    var mockPlayerService = {
      currentID: function () {
        return 'MANUAL1';
      },
      currentPlayerBaseURL: function () {
        return '/api/player/MANUAL1';
      },
      currentPlayerFriends: function () {
        friendsDeferred = q.defer();
        return friendsDeferred.promise;
      }
    };

    ctrl = $controller('CreateCtrl', {
      $scope: scope,
      twGameCache: gameCache,
      twGameFeatureService: mockFeatureService,
      twCurrentPlayerService: mockPlayerService,
      $location: location
    });

    featureDeferred.resolve({});
    friendsDeferred.resolve(friends);
    rootScope.$apply();
  }));

  it('initializes', function () {
    expect(scope.alerts).toEqual([]);
    expect(scope.thieving).toEqual('Thieving');
    expect(scope.drawGallows).toEqual('');
    expect(scope.drawFace).toEqual('');
    expect(scope.gamePace).toEqual('Live');

    expect(scope.desiredPlayerCount).toEqual('SinglePlayer');
    expect(scope.friends).toEqual([
      {md5: 'md1', displayName: 'friend1'},
      {md5: 'md2', displayName: 'friend2'},
      {md5: 'md3', displayName: 'friend3'},
      {md5: 'md4', displayName: 'friend4'}
    ]);
    expect(scope.gamePace).toEqual('Live');
    expect(scope.wordPhraseSetter).toEqual('SystemPuzzles');
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
    scope.desiredPlayerCount = 'X';
    scope.gamePace = 'TurnBased';
    scope.playerChoices.push(scope.friends[1]);
    scope.$apply();
    scope.wordPhraseSetter = 'Y';
    scope.winners = 'X';
    scope.h2hEnabled = true;
    scope.alternatingEnabled = true;
    scope.alternatingEnabled = true;
    scope.turnBasedEnabled = true;

    scope.setSinglePlayer();

    expect(scope.thieving).toEqual('');
    expect(scope.drawGallows).toEqual('DrawGallows');
    expect(scope.drawFace).toEqual('DrawFace');
    expect(scope.playerChoices).toEqual([]);
    expect(scope.gamePace).toEqual('Live');
    expect(scope.desiredPlayerCount).toEqual('SinglePlayer');
    expect(scope.wordPhraseSetter).toEqual('SystemPuzzles');
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
    scope.wordPhraseSetter = 'SystemPuzzles';
    scope.winners = 'AllComplete';

    //  should change
    scope.desiredPlayerCount = 'X';
    scope.playerChoices.push(scope.friends[1]);
    scope.playerChoices.push(scope.friends[2]);
    scope.$apply();
    scope.h2hEnabled = false;
    scope.alternatingEnabled = false;
    scope.alternatingEnabled = false;
    scope.turnBasedEnabled = false;

    scope.setTwoPlayers();

    expect(scope.thieving).toEqual('');
    expect(scope.drawGallows).toEqual('DrawGallows');
    expect(scope.drawFace).toEqual('DrawFace');
    expect(scope.gamePace).toEqual('TurnBased');

    expect(scope.desiredPlayerCount).toEqual('TwoPlayer');
    expect(scope.playerChoices).toEqual([]);
    expect(scope.gamePace).toEqual('TurnBased');
    expect(scope.wordPhraseSetter).toEqual('SystemPuzzles');
    expect(scope.winners).toEqual('AllComplete');
    expect(scope.h2hEnabled).toBe(true);
    expect(scope.alternatingEnabled).toBe(true);
    expect(scope.allFinishedEnabled).toBe(true);
    expect(scope.turnBasedEnabled).toBe(true);
    expect(scope.submitEnabled).toBe(false);
  });

  it('changes to two player from multiplayer with only one opponent', function () {
    scope.playerChoices.push(scope.friends[1]);
    scope.$apply();
    scope.setTwoPlayers();
    expect(scope.playerChoices).toEqual([scope.friends[1]]);
    expect(scope.submitEnabled).toBe(true);
  });

  it('changes to multi player from multiplayer', function () {
    //  should not change
    scope.thieving = '';
    scope.drawGallows = 'DrawGallows';
    scope.drawFace = 'DrawFace';
    scope.gamePace = 'TurnBased';
    scope.winners = 'AllComplete';
    scope.playerChoices.push(scope.friends[0]);
    scope.playerChoices.push(scope.friends[1]);
    scope.$apply();

    //  should change
    scope.wordPhraseSetter = 'Head2Head';
    scope.desiredPlayerCount = 'X';
    scope.h2hEnabled = true;
    scope.alternatingEnabled = false;
    scope.alternatingEnabled = false;
    scope.turnBasedEnabled = false;

    scope.setThreePlayers();

    expect(scope.thieving).toEqual('');
    expect(scope.drawGallows).toEqual('DrawGallows');
    expect(scope.drawFace).toEqual('DrawFace');
    expect(scope.gamePace).toEqual('TurnBased');

    expect(scope.desiredPlayerCount).toEqual('ThreePlus');
    expect(scope.playerChoices).toEqual([scope.friends[0], scope.friends[1]]);
    expect(scope.gamePace).toEqual('TurnBased');
    expect(scope.wordPhraseSetter).toEqual('SystemPuzzles');
    expect(scope.winners).toEqual('AllComplete');
    expect(scope.h2hEnabled).toBe(false);
    expect(scope.alternatingEnabled).toBe(true);
    expect(scope.allFinishedEnabled).toBe(true);
    expect(scope.turnBasedEnabled).toBe(true);
    expect(scope.submitEnabled).toBe(true);

  });

  it('test submit enable for single player game', function () {
    //  Hopefully not possible to achieve this state in the first place
    //  but added protection to make sure button is disabled
    scope.setSinglePlayer();
    expect(scope.submitEnabled).toBe(true);

    scope.playerChoices.push(scope.friends[0]);
    scope.$apply();

    expect(scope.playerChoices).toEqual([scope.friends[0]]);
    expect(scope.submitEnabled).toBe(false);
  });


  it('test submit enable for two player game', function () {
    scope.setTwoPlayers();
    scope.$apply();
    expect(scope.playerChoices).toEqual([]);
    expect(scope.submitEnabled).toBe(false);

    scope.playerChoices.push(scope.friends[0]);
    scope.$apply();

    expect(scope.playerChoices).toEqual([scope.friends[0]]);
    expect(scope.submitEnabled).toBe(true);

    scope.playerChoices.push(scope.friends[1]);
    scope.$apply();

    expect(scope.playerChoices).toEqual([scope.friends[0], scope.friends[1]]);
    expect(scope.submitEnabled).toBe(false);

    scope.playerChoices.splice(0, 1);
    scope.$apply();

    expect(scope.playerChoices).toEqual([scope.friends[1]]);
    expect(scope.submitEnabled).toBe(true);
  });

  it('test submit enable for multi player game', function () {
    scope.setThreePlayers();
    scope.$apply();
    expect(scope.playerChoices).toEqual([]);
    expect(scope.submitEnabled).toBe(false);

    scope.playerChoices.push(scope.friends[0]);
    scope.$apply();
    expect(scope.playerChoices).toEqual([scope.friends[0]]);
    expect(scope.submitEnabled).toBe(false);

    scope.playerChoices.push(scope.friends[1]);
    scope.$apply();
    expect(scope.playerChoices).toEqual([scope.friends[0], scope.friends[1]]);
    expect(scope.submitEnabled).toBe(true);

    scope.playerChoices.push(scope.friends[2]);
    scope.$apply();
    expect(scope.playerChoices).toEqual([scope.friends[0], scope.friends[1], scope.friends[2]]);
    expect(scope.submitEnabled).toBe(true);
  });

  it('test create game submission sp', function () {
    scope.setSinglePlayer();
    var varid = 'anid';
    var createdGame = {gamePhase: 'test', id: varid};
    http.expectPOST('/api/player/MANUAL1/new', {
      players: [],
      features: ['SystemPuzzles', 'SinglePlayer', 'Thieving', 'Live', 'SingleWinner']
    }).respond(createdGame);
    scope.createGame();
    http.flush();
    expect(gameCache.putUpdatedGame).toHaveBeenCalledWith(createdGame);
    expect(location.path).toHaveBeenCalledWith('/show/anid');
  });

  it('test create game submission 2player', function () {
    scope.setTwoPlayers();
    scope.thieving = '';
    scope.playerChoices = [{md5: 'x'}];
    scope.winners = 'SingleWinner';
    scope.gamePace = 'TurnBased';
    scope.wordPhraseSetter = 'Head2Head';
    var varid = 'anid';
    var createdGame = {gamePhase: 'test2', id: varid};
    http.expectPOST('/api/player/MANUAL1/new', {
      players: ['x'],
      features: ['Head2Head', 'TwoPlayer', 'TurnBased', 'SingleWinner']
    }).respond(createdGame);
    scope.createGame();
    http.flush();
    expect(gameCache.putUpdatedGame).toHaveBeenCalledWith(createdGame);
    expect(location.path).toHaveBeenCalledWith('/show/anid');
  });

  it('test create game submission 3+player', function () {
    scope.setThreePlayers();
    scope.thieving = '';
    scope.playerChoices = [{md5: 'x'}, {md5: 'y'}];
    scope.winners = 'AllComplete';
    scope.gamePace = 'TurnBased';
    var varid = 'anid';
    var createdGame = {gamePhase: 'test3', id: varid};
    http.expectPOST('/api/player/MANUAL1/new', {
      players: ['x', 'y'],
      features: ['SystemPuzzles', 'ThreePlus', 'TurnBased', 'AllComplete']
    }).respond(createdGame);
    scope.createGame();
    http.flush();
    expect(gameCache.putUpdatedGame).toHaveBeenCalledWith(createdGame);
    expect(location.path).toHaveBeenCalledWith('/show/anid');
  });

  it('test closing alerts', function () {
    scope.alerts = ['1', '2'];
    scope.closeAlert(0);
    expect(scope.alerts).toEqual(['2']);
  });

  it('test closing alerts with bad values', function () {
    scope.alerts = ['1', '2'];
    scope.closeAlert(-1);
    expect(scope.alerts).toEqual(['1', '2']);
    scope.closeAlert(2);
    expect(scope.alerts).toEqual(['1', '2']);
    scope.closeAlert();
    expect(scope.alerts).toEqual(['1', '2']);
  });

  it('test alerts game submission failure', function () {
    scope.setThreePlayers();
    scope.thieving = '';
    scope.playerChoices = [{md5: 'x'}, {md5: 'y'}];
    scope.winners = 'AllComplete';
    scope.gamePace = 'Live';
    http.expectPOST('/api/player/MANUAL1/new', {
      players: ['x', 'y'],
      features: ['SystemPuzzles', 'ThreePlus', 'Live', 'AllComplete']
    }).respond(404, 'badstuff');
    scope.createGame();
    http.flush();
    expect(scope.alerts).toEqual([{type: 'danger', msg: 'Error creating game - 404:badstuff'}]);
  });
});

