'use strict';

describe('Controller: CreateCtrl', function () {

  // load the controller's module
  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, http, q, rootScope, featureDeferred, location, friendsDeferred;
  var friends = {md1: 'friend1', md2: 'friend2', md3: 'friend3', md4: 'friend4'};

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
    rootScope = $rootScope;
    http = $httpBackend;
    q = $q;
    spyOn(rootScope, '$broadcast');
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
      twGameFeatureService: mockFeatureService,
      twCurrentPlayerService: mockPlayerService,
      $location: location
    });

    featureDeferred.resolve({});
    friendsDeferred.resolve(friends);
    rootScope.$apply();
  }));

  it('initializes', function () {
    expect(scope.url).toEqual('/api/player/MANUAL1/new');
    expect(scope.thieving).toEqual('Thieving');
    expect(scope.drawGallows).toEqual('');
    expect(scope.drawFace).toEqual('');
    expect(scope.gamePace).toEqual('');

    expect(scope.playerCount).toEqual('SinglePlayer');
    expect(scope.players).toEqual([]);
    expect(scope.friends).toEqual([
      {md5: 'md1', name: 'friend1', selected: false, enabled: false},
      {md5: 'md2', name: 'friend2', selected: false, enabled: false},
      {md5: 'md3', name: 'friend3', selected: false, enabled: false},
      {md5: 'md4', name: 'friend4', selected: false, enabled: false}
    ]);
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
    scope.players = ['md1'];
    scope.friends[0].selected = true;
    scope.friends.forEach(function (friend) {
      friend.enabled = true;
    });
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
    scope.friends.forEach(function (friend) {
      expect(friend.enabled).toEqual(false);
      expect(friend.selected).toEqual(false);
    });

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
    scope.players = ['md1', 'md2'];
    scope.friends[0].selected = true;
    scope.friends[0].enabled = true;
    scope.friends[1].selected = true;
    scope.friends[1].enabled = true;
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
    scope.friends.forEach(function (friend) {
      expect(friend.enabled).toEqual(true);
      expect(friend.selected).toEqual(false);
    });
  });

  it('changes to two player from multiplayer with only one opponent', function () {
    scope.players = ['md2'];
    scope.friends[1].selected = true;
    scope.friends[1].enabled = true;
    scope.setTwoPlayers();
    expect(scope.players).toEqual(['md2']);
    expect(scope.submitEnabled).toBe(true);
    scope.friends.forEach(function (friend) {
      if (friend.md5 === 'md2') {
        expect(friend.enabled).toEqual(true);
        expect(friend.selected).toEqual(true);
      } else {
        expect(friend.enabled).toEqual(false);
        expect(friend.selected).toEqual(false);
      }
    });
  });

  it('changes to multi player from multiplayer', function () {
    //  should not change
    scope.thieving = '';
    scope.drawGallows = 'DrawGallows';
    scope.drawFace = 'DrawFace';
    scope.gamePace = 'TurnBased';
    scope.winners = '';
    scope.players = ['md1', 'md2'];
    scope.friends[0].selected = true;
    scope.friends[0].enabled = true;
    scope.friends[1].selected = true;
    scope.friends[1].enabled = true;

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
    expect(scope.players).toEqual(['md1', 'md2']);
    expect(scope.gamePace).toEqual('TurnBased');
    expect(scope.puzzleSetter).toEqual('SystemPuzzles');
    expect(scope.winners).toEqual('');
    expect(scope.h2hEnabled).toBe(false);
    expect(scope.alternatingEnabled).toBe(true);
    expect(scope.allFinishedEnabled).toBe(true);
    expect(scope.turnBasedEnabled).toBe(true);
    expect(scope.submitEnabled).toBe(true);

    scope.friends.forEach(function (friend) {
      if (friend.md5 === 'md2' || friend.md5 === 'md1') {
        expect(friend.enabled).toEqual(true);
        expect(friend.selected).toEqual(true);
      } else {
        expect(friend.enabled).toEqual(true);
        expect(friend.selected).toEqual(false);
      }
    });
  });

  it('test submit enable for single player game', function () {
    //  Hopefully not possible to achieve this state in the first place
    //  but added protection to make sure button is disabled
    scope.setSinglePlayer();
    expect(scope.players).toEqual([]);
    expect(scope.submitEnabled).toBe(true);

    scope.friends[0].selected = true;
    scope.changePlayer();
    expect(scope.players).toEqual(['md1']);
    expect(scope.submitEnabled).toBe(false);
  });


  it('test submit enable for two player game', function () {
    scope.setTwoPlayers();
    expect(scope.players).toEqual([]);
    expect(scope.submitEnabled).toBe(false);

    scope.friends[0].selected = true;
    scope.changePlayer();
    expect(scope.players).toEqual(['md1']);
    expect(scope.submitEnabled).toBe(true);
  });

  it('test submit enable for multi player game', function () {
    scope.setThreePlayers();
    expect(scope.players).toEqual([]);
    expect(scope.submitEnabled).toBe(false);

    scope.friends[0].selected = true;
    scope.changePlayer();
    expect(scope.players).toEqual(['md1']);
    expect(scope.submitEnabled).toBe(false);

    scope.friends[0].selected = true;
    scope.friends[1].selected = true;
    scope.changePlayer();
    expect(scope.players).toEqual(['md1', 'md2']);
    expect(scope.submitEnabled).toBe(true);

    scope.friends[0].selected = true;
    scope.friends[1].selected = true;
    scope.friends[2].selected = true;
    scope.changePlayer();
    expect(scope.players).toEqual(['md1', 'md2', 'md3']);
    expect(scope.submitEnabled).toBe(true);
  });

  it('test create game submission sp', function () {
    scope.setSinglePlayer();
    var varid = 'anid';
    http.expectPOST('/api/player/MANUAL1/new', {
      players: [],
      features: ['SystemPuzzles', 'SinglePlayer', 'Thieving', 'SingleWinner']
    }).respond({gamePhase: 'test', id: varid});
    scope.createGame();
    http.flush();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'test');
    expect(location.path).toHaveBeenCalledWith('/show/anid');
  });

  it('test create game submission 2player', function () {
    scope.setTwoPlayers();
    scope.thieving = '';
    scope.players = ['x'];
    scope.winners = 'SingleWinner';
    scope.gamePace = 'TurnBased';
    scope.puzzleSetter = '';
    var varid = 'anid';
    http.expectPOST('/api/player/MANUAL1/new', {
      players: ['x'],
      features: ['TwoPlayer', 'TurnBased', 'SingleWinner']
    }).respond({gamePhase: 'test2', id: varid});
    scope.createGame();
    http.flush();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'test2');
    expect(location.path).toHaveBeenCalledWith('/show/anid');
  });

  it('test create game submission 3+player', function () {
    scope.setThreePlayers();
    scope.thieving = '';
    scope.players = ['x', 'y'];
    scope.winners = '';
    scope.gamePace = '';
    var varid = 'anid';
    http.expectPOST('/api/player/MANUAL1/new', {
      players: ['x', 'y'],
      features: ['SystemPuzzles', 'ThreePlus']
    }).respond({gamePhase: 'test3', id: varid});
    scope.createGame();
    http.flush();
    expect(rootScope.$broadcast).toHaveBeenCalledWith('refreshGames', 'test3');
    expect(location.path).toHaveBeenCalledWith('/show/anid');
  });

});

