'use strict';

describe('Controller: CreateCtrl', function () {

  beforeEach(module('twistedHangmanApp'));

  var ctrl, scope, q, rootScope, featureDeferred;
  var jtbGameActions = {
    new: jasmine.createSpy('new')
  };

  var longName = 'AppName';
  var manualPlayer = {source: 'MANUAL'};
  var currentPlayer = manualPlayer;

  var mockPlayerService = {
    currentPlayer: function () {
      return currentPlayer;
    },
    currentID: function () {
      return 'MANUAL1';
    },
    currentPlayerBaseURL: function () {
      return '/api/player/MANUAL1';
    },
    initializeFriendsForController: jasmine.createSpy('initializeFriends')
  };

  var mockFacebook = {
    inviteFriends: jasmine.createSpy('inviteFriends')
  };

  var mockFeatureService = {
    features: function () {
      featureDeferred = q.defer();
      return featureDeferred.promise;
    }
  };

  beforeEach(inject(function ($rootScope, $q, $controller) {
    rootScope = $rootScope;
    q = $q;
    spyOn(rootScope, '$broadcast').and.callThrough();
    scope = rootScope.$new();
    jtbGameActions.new.calls.reset();
    ctrl = $controller('CreateCtrl', {
      $scope: scope,
      jtbAppLongName: longName,
      twGameFeatureService: mockFeatureService,
      jtbPlayerService: mockPlayerService,
      jtbBootstrapGameActions: jtbGameActions,
      jtbFacebook: mockFacebook
    });
  }));

  describe('core initialization tests', function () {

    it('initializes', function () {
      var featureData = {X: 'Y'};
      featureDeferred.resolve(featureData);
      rootScope.$apply();

      expect(ctrl.featureData).toEqual(featureData);
      expect(ctrl.thieving).toEqual('Thieving');
      expect(ctrl.drawGallows).toEqual('');
      expect(ctrl.drawFace).toEqual('DrawFace');
      expect(ctrl.gamePace).toEqual('Live');

      expect(ctrl.desiredPlayerCount).toEqual('SinglePlayer');
      expect(
        mockPlayerService.initializeFriendsForController).toHaveBeenCalledWith(
        ctrl);
      expect(ctrl.gamePace).toEqual('Live');
      expect(ctrl.wordPhraseSetter).toEqual('SystemPuzzles');
      expect(ctrl.winners).toEqual('SingleWinner');
      expect(ctrl.h2hEnabled).toBe(false);
      expect(ctrl.alternatingEnabled).toBe(false);
      expect(ctrl.allFinishedEnabled).toBe(false);
      expect(ctrl.turnBasedEnabled).toBe(false);
      expect(ctrl.submitEnabled).toBe(true);
    });

  });

  describe('additional fb initialization tests', function () {

    it('show invite opens dialog', function () {
      ctrl.showInvite();
      expect(mockFacebook.inviteFriends).toHaveBeenCalledWith([],
        'Come play AppName with me!');
    });
  });

  describe('post initialization', function () {
    beforeEach(function () {
      ctrl.chosenFriends = [];
      ctrl.friends = [
        {md5: 'md1', displayName: 'friend1'},
        {md5: 'md2', displayName: 'friend2'},
        {md5: 'md3', displayName: 'friend3'},
        {md5: 'md4', displayName: 'friend4'}
      ];
      featureDeferred.resolve({});
      rootScope.$apply();
    });

    it('resets to single player', function () {
      //  should not change
      ctrl.thieving = '';
      ctrl.drawGallows = 'DrawGallows';
      ctrl.drawFace = '';

      //  should change
      ctrl.desiredPlayerCount = 'X';
      ctrl.gamePace = 'TurnBased';
      ctrl.chosenFriends.push(ctrl.friends[1]);
      scope.$apply();
      ctrl.wordPhraseSetter = 'Y';
      ctrl.winners = 'X';
      ctrl.h2hEnabled = true;
      ctrl.alternatingEnabled = true;
      ctrl.alternatingEnabled = true;
      ctrl.turnBasedEnabled = true;

      ctrl.setSinglePlayer();

      expect(ctrl.thieving).toEqual('');
      expect(ctrl.drawGallows).toEqual('DrawGallows');
      expect(ctrl.drawFace).toEqual('');
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.gamePace).toEqual('Live');
      expect(ctrl.desiredPlayerCount).toEqual('SinglePlayer');
      expect(ctrl.wordPhraseSetter).toEqual('SystemPuzzles');
      expect(ctrl.winners).toEqual('SingleWinner');
      expect(ctrl.h2hEnabled).toBe(false);
      expect(ctrl.alternatingEnabled).toBe(false);
      expect(ctrl.allFinishedEnabled).toBe(false);
      expect(ctrl.turnBasedEnabled).toBe(false);
      expect(ctrl.submitEnabled).toBe(true);
    });

    it('changes to two player from multiplayer', function () {
      //  should not change
      ctrl.thieving = '';
      ctrl.drawGallows = 'DrawGallows';
      ctrl.drawFace = '';
      ctrl.gamePace = 'TurnBased';
      ctrl.wordPhraseSetter = 'SystemPuzzles';
      ctrl.winners = 'AllComplete';

      //  should change
      ctrl.desiredPlayerCount = 'X';
      ctrl.chosenFriends.push(ctrl.friends[1]);
      ctrl.chosenFriends.push(ctrl.friends[2]);
      scope.$apply();
      ctrl.h2hEnabled = false;
      ctrl.alternatingEnabled = false;
      ctrl.alternatingEnabled = false;
      ctrl.turnBasedEnabled = false;

      ctrl.setTwoPlayers();

      expect(ctrl.thieving).toEqual('');
      expect(ctrl.drawGallows).toEqual('DrawGallows');
      expect(ctrl.drawFace).toEqual('');
      expect(ctrl.gamePace).toEqual('TurnBased');

      expect(ctrl.desiredPlayerCount).toEqual('TwoPlayer');
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.gamePace).toEqual('TurnBased');
      expect(ctrl.wordPhraseSetter).toEqual('SystemPuzzles');
      expect(ctrl.winners).toEqual('AllComplete');
      expect(ctrl.h2hEnabled).toBe(true);
      expect(ctrl.alternatingEnabled).toBe(true);
      expect(ctrl.allFinishedEnabled).toBe(true);
      expect(ctrl.turnBasedEnabled).toBe(true);
      expect(ctrl.submitEnabled).toBe(false);
    });

    it('changes to two player from multiplayer with only one opponent',
      function () {
        ctrl.chosenFriends.push(ctrl.friends[1]);
        scope.$apply();
        ctrl.setTwoPlayers();
        expect(ctrl.chosenFriends).toEqual([ctrl.friends[1]]);
        expect(ctrl.submitEnabled).toBe(true);
      });

    it('changes to multi player from multiplayer', function () {
      //  should not change
      ctrl.thieving = '';
      ctrl.drawGallows = 'DrawGallows';
      ctrl.drawFace = '';
      ctrl.gamePace = 'TurnBased';
      ctrl.winners = 'AllComplete';
      ctrl.chosenFriends.push(ctrl.friends[0]);
      ctrl.chosenFriends.push(ctrl.friends[1]);
      scope.$apply();

      //  should change
      ctrl.wordPhraseSetter = 'Head2Head';
      ctrl.desiredPlayerCount = 'X';
      ctrl.h2hEnabled = true;
      ctrl.alternatingEnabled = false;
      ctrl.alternatingEnabled = false;
      ctrl.turnBasedEnabled = false;

      ctrl.setThreePlayers();

      expect(ctrl.thieving).toEqual('');
      expect(ctrl.drawGallows).toEqual('DrawGallows');
      expect(ctrl.drawFace).toEqual('');
      expect(ctrl.gamePace).toEqual('TurnBased');

      expect(ctrl.desiredPlayerCount).toEqual('ThreePlus');
      expect(ctrl.chosenFriends).toEqual([ctrl.friends[0], ctrl.friends[1]]);
      expect(ctrl.gamePace).toEqual('TurnBased');
      expect(ctrl.wordPhraseSetter).toEqual('SystemPuzzles');
      expect(ctrl.winners).toEqual('AllComplete');
      expect(ctrl.h2hEnabled).toBe(false);
      expect(ctrl.alternatingEnabled).toBe(true);
      expect(ctrl.allFinishedEnabled).toBe(true);
      expect(ctrl.turnBasedEnabled).toBe(true);
      expect(ctrl.submitEnabled).toBe(true);
    });

    it('test submit enable for single player game', function () {
      //  Hopefully not possible to achieve this state in the first place
      //  but added protection to make sure button is disabled
      ctrl.setSinglePlayer();
      expect(ctrl.submitEnabled).toBe(true);

      ctrl.chosenFriends.push(ctrl.friends[0]);
      scope.$apply();

      expect(ctrl.chosenFriends).toEqual([ctrl.friends[0]]);
      expect(ctrl.submitEnabled).toBe(false);

      ctrl.clearPlayers();
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.submitEnabled).toBe(true);
    });

    it('test submit enable for two player game', function () {
      ctrl.setTwoPlayers();
      scope.$apply();
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.submitEnabled).toBe(false);

      ctrl.chosenFriends.push(ctrl.friends[0]);
      scope.$apply();

      expect(ctrl.chosenFriends).toEqual([ctrl.friends[0]]);
      expect(ctrl.submitEnabled).toBe(true);

      ctrl.chosenFriends.push(ctrl.friends[1]);
      scope.$apply();

      expect(ctrl.chosenFriends).toEqual([ctrl.friends[0], ctrl.friends[1]]);
      expect(ctrl.submitEnabled).toBe(false);

      ctrl.chosenFriends.splice(0, 1);
      scope.$apply();

      expect(ctrl.chosenFriends).toEqual([ctrl.friends[1]]);
      expect(ctrl.submitEnabled).toBe(true);

      ctrl.clearPlayers();
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.submitEnabled).toBe(false);
    });

    it('test submit enable for multi player game', function () {
      ctrl.setThreePlayers();
      scope.$apply();
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.submitEnabled).toBe(false);

      ctrl.chosenFriends.push(ctrl.friends[0]);
      scope.$apply();
      expect(ctrl.chosenFriends).toEqual([ctrl.friends[0]]);
      expect(ctrl.submitEnabled).toBe(false);

      ctrl.chosenFriends.push(ctrl.friends[1]);
      scope.$apply();
      expect(ctrl.chosenFriends).toEqual([ctrl.friends[0], ctrl.friends[1]]);
      expect(ctrl.submitEnabled).toBe(true);

      ctrl.chosenFriends.push(ctrl.friends[2]);
      scope.$apply();
      expect(ctrl.chosenFriends).toEqual(
        [ctrl.friends[0], ctrl.friends[1], ctrl.friends[2]]);
      expect(ctrl.submitEnabled).toBe(true);

      ctrl.clearPlayers();
      expect(ctrl.chosenFriends).toEqual([]);
      expect(ctrl.submitEnabled).toBe(false);
    });

    it('test create game submission sp', function () {
      ctrl.setSinglePlayer();
      ctrl.createGame();
      expect(jtbGameActions.new).toHaveBeenCalledWith({
        players: [],
        features: ['SystemPuzzles', 'SinglePlayer', 'Thieving', 'DrawFace',
          'Live', 'SingleWinner']
      });
    });

    it('test create game submission 2player', function () {
      ctrl.setTwoPlayers();
      ctrl.thieving = '';
      ctrl.chosenFriends = [{md5: 'x'}];
      ctrl.winners = 'SingleWinner';
      ctrl.gamePace = 'TurnBased';
      ctrl.drawFace = '';
      ctrl.wordPhraseSetter = 'Head2Head';
      ctrl.createGame();
      expect(jtbGameActions.new).toHaveBeenCalledWith({
        players: ['x'],
        features: ['Head2Head', 'TwoPlayer', 'TurnBased', 'SingleWinner']
      });
    });

    it('test create game submission 3+player', function () {
      ctrl.setThreePlayers();
      ctrl.thieving = '';
      ctrl.chosenFriends = [{md5: 'x'}, {md5: 'y'}];
      ctrl.winners = 'AllComplete';
      ctrl.gamePace = 'TurnBased';
      ctrl.createGame();
      expect(jtbGameActions.new).toHaveBeenCalledWith({
        players: ['x', 'y'],
        features: ['SystemPuzzles', 'ThreePlus', 'DrawFace', 'TurnBased',
          'AllComplete']
      });
    });
  });
});

