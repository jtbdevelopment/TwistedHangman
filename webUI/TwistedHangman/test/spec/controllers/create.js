'use strict';

describe('Controller: CreateCtrl', function () {

    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, scope, q, rootScope, featureDeferred, friendsDeferred;
    var jtbGameActions = {
        new: jasmine.createSpy('new')
    };
    var longName = 'AppName';
    var friends = {
        maskedFriends: {
            md1: 'friend1',
            md2: 'friend2',
            md3: 'friend3',
            md4: 'friend4'
        },
        invitableFriends: [
            {
                id: 'if1',
                name: 'ifriend1'
            },
            {
                id: 'if2',
                name: 'ifriend2',
                picture: {}
            },
            {
                id: 'if3',
                name: 'ifriend3',
                picture: {
                    url: 'http://ifriend3image.png'
                }
            }
        ]


    };
    var manualPlayer = {source: 'MANUAL'};
    var fbPlayer = {source: 'facebook'};
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
        currentPlayerFriends: function () {
            friendsDeferred = q.defer();
            return friendsDeferred.promise;
        }
    };

    var mockFeatureService = {
        features: function () {
            featureDeferred = q.defer();
            return featureDeferred.promise;
        }
    };

    var adPopupModalResult, ads, adsCalled;
    ads = {
        showAdPopup: function () {
            adPopupModalResult = q.defer();
            adsCalled = true;
            return adPopupModalResult.promise;
        }
    };

    // Initialize the controller and a mock scope
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
            twAds: ads,
            jtbBootstrapGameActions: jtbGameActions
        });

        adsCalled = false;
    }));

    describe('core initialization tests', function () {

        it('initializes', function () {
            var featureData = {X: 'Y'};
            featureDeferred.resolve(featureData);
            friendsDeferred.resolve(friends);
            rootScope.$apply();

            expect(scope.featureData).toEqual(featureData);
            expect(scope.thieving).toEqual('Thieving');
            expect(scope.drawGallows).toEqual('');
            expect(scope.drawFace).toEqual('DrawFace');
            expect(scope.gamePace).toEqual('Live');

            expect(scope.desiredPlayerCount).toEqual('SinglePlayer');
            expect(scope.friends).toEqual([
                {md5: 'md1', displayName: 'friend1'},
                {md5: 'md2', displayName: 'friend2'},
                {md5: 'md3', displayName: 'friend3'},
                {md5: 'md4', displayName: 'friend4'}
            ]);
            expect(scope.invitableFBFriends).toEqual([]);
            expect(scope.gamePace).toEqual('Live');
            expect(scope.wordPhraseSetter).toEqual('SystemPuzzles');
            expect(scope.winners).toEqual('SingleWinner');
            expect(scope.h2hEnabled).toBe(false);
            expect(scope.alternatingEnabled).toBe(false);
            expect(scope.allFinishedEnabled).toBe(false);
            expect(scope.turnBasedEnabled).toBe(false);
            expect(scope.submitEnabled).toBe(true);
            expect(adsCalled).toEqual(false);
        });

    });

    describe('additional fb initialization tests', function () {
        var modalOpened;
        var modal = {
            open: function (params) {
                expect(params.controller).toEqual('CoreBootstrapInviteCtrl');
                expect(params.templateUrl).toEqual('views/core-bs/friends/invite-friends.html');
                expect(params.controllerAs).toEqual('invite');
                expect(params.size).toEqual('lg');
                expect(params.resolve.invitableFriends()).toEqual(scope.invitableFBFriends);
                expect(params.resolve.message()).toEqual('Come play ' + longName + ' with me!');
                modalOpened = true;
            }
        };

        beforeEach(inject(function ($rootScope, $httpBackend, $q, $controller) {
            currentPlayer = fbPlayer;
            modalOpened = false;
            jtbGameActions.new.calls.reset();
            ctrl = $controller('CreateCtrl', {
                $scope: scope,
                jtbAppLongName: longName,
                twGameFeatureService: mockFeatureService,
                jtbPlayerService: mockPlayerService,
                jtbBootstrapGameActions: jtbGameActions,
                $uibModal: modal
            });
        }));

        it('initializes invitable friends too', function () {
            var featureData = {X: 'Y'};
            featureDeferred.resolve(featureData);
            friendsDeferred.resolve(friends);
            rootScope.$apply();

            expect(scope.featureData).toEqual(featureData);
            expect(scope.thieving).toEqual('Thieving');
            expect(scope.drawGallows).toEqual('');
            expect(scope.drawFace).toEqual('DrawFace');
            expect(scope.gamePace).toEqual('Live');

            expect(scope.desiredPlayerCount).toEqual('SinglePlayer');
            expect(scope.friends).toEqual([
                {md5: 'md1', displayName: 'friend1'},
                {md5: 'md2', displayName: 'friend2'},
                {md5: 'md3', displayName: 'friend3'},
                {md5: 'md4', displayName: 'friend4'}
            ]);
            expect(scope.invitableFBFriends).toEqual([
                {id: 'if1', name: 'ifriend1'},
                {id: 'if2', name: 'ifriend2'},
                {id: 'if3', name: 'ifriend3', url: 'http://ifriend3image.png'}
            ]);
            expect(scope.gamePace).toEqual('Live');
            expect(scope.wordPhraseSetter).toEqual('SystemPuzzles');
            expect(scope.winners).toEqual('SingleWinner');
            expect(scope.h2hEnabled).toBe(false);
            expect(scope.alternatingEnabled).toBe(false);
            expect(scope.allFinishedEnabled).toBe(false);
            expect(scope.turnBasedEnabled).toBe(false);
            expect(scope.submitEnabled).toBe(true);
            expect(adsCalled).toEqual(false);
        });

        it('show invite opens dialog', function () {
            scope.showInvite();
            expect(modalOpened).toEqual(true);
            expect(adsCalled).toEqual(false);
        });
    });

    describe('post initialization', function () {
        beforeEach(function () {
            featureDeferred.resolve({});
            friendsDeferred.resolve(friends);
            rootScope.$apply();
        });

        it('resets to single player', function () {
            //  should not change
            scope.thieving = '';
            scope.drawGallows = 'DrawGallows';
            scope.drawFace = '';

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
            expect(scope.drawFace).toEqual('');
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
            expect(adsCalled).toEqual(false);
        });

        it('changes to two player from multiplayer', function () {
            //  should not change
            scope.thieving = '';
            scope.drawGallows = 'DrawGallows';
            scope.drawFace = '';
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
            expect(scope.drawFace).toEqual('');
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
            expect(adsCalled).toEqual(false);
        });

        it('changes to two player from multiplayer with only one opponent', function () {
            scope.playerChoices.push(scope.friends[1]);
            scope.$apply();
            scope.setTwoPlayers();
            expect(scope.playerChoices).toEqual([scope.friends[1]]);
            expect(scope.submitEnabled).toBe(true);
            expect(adsCalled).toEqual(false);
        });

        it('changes to multi player from multiplayer', function () {
            //  should not change
            scope.thieving = '';
            scope.drawGallows = 'DrawGallows';
            scope.drawFace = '';
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
            expect(scope.drawFace).toEqual('');
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
            expect(adsCalled).toEqual(false);
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

            scope.clearPlayers();
            expect(scope.playerChoices).toEqual([]);
            expect(scope.submitEnabled).toBe(true);
            expect(adsCalled).toEqual(false);
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

            scope.clearPlayers();
            expect(scope.playerChoices).toEqual([]);
            expect(scope.submitEnabled).toBe(false);
            expect(adsCalled).toEqual(false);
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

            scope.clearPlayers();
            expect(scope.playerChoices).toEqual([]);
            expect(scope.submitEnabled).toBe(false);
            expect(adsCalled).toEqual(false);
        });

        it('test create game submission sp', function () {
            scope.setSinglePlayer();
            scope.createGame();
            adPopupModalResult.resolve();
            rootScope.$apply();
            expect(jtbGameActions.new).toHaveBeenCalledWith({
                players: [],
                features: ['SystemPuzzles', 'SinglePlayer', 'Thieving', 'DrawFace', 'Live', 'SingleWinner']
            });
            expect(adsCalled).toEqual(true);
        });

        it('test create game submission 2player', function () {
            scope.setTwoPlayers();
            scope.thieving = '';
            scope.playerChoices = [{md5: 'x'}];
            scope.winners = 'SingleWinner';
            scope.gamePace = 'TurnBased';
            scope.drawFace = '';
            scope.wordPhraseSetter = 'Head2Head';
            scope.createGame();
            adPopupModalResult.resolve();
            rootScope.$apply();
            expect(adsCalled).toEqual(true);
            expect(jtbGameActions.new).toHaveBeenCalledWith({
                players: ['x'],
                features: ['Head2Head', 'TwoPlayer', 'TurnBased', 'SingleWinner']
            });
        });

        it('test create game submission 3+player', function () {
            scope.setThreePlayers();
            scope.thieving = '';
            scope.playerChoices = [{md5: 'x'}, {md5: 'y'}];
            scope.winners = 'AllComplete';
            scope.gamePace = 'TurnBased';
            scope.createGame();
            adPopupModalResult.resolve();
            rootScope.$apply();
            expect(adsCalled).toEqual(true);
            expect(jtbGameActions.new).toHaveBeenCalledWith({
                players: ['x', 'y'],
                features: ['SystemPuzzles', 'ThreePlus', 'DrawFace', 'TurnBased', 'AllComplete']
            });
        });
    });
});

