'use strict';

describe('Controller: ShowCtrl', function () {
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

    var player = {'player': 'player'};
    var ctrl, $scope, $http, $rootScope, gameDisplay, $q, $location, controller, $timeout;
    var adPopupModalResult, ads, adsCalled;
    var gameCacheExpectedId, gameCacheReturnResult, mockPlayerService, mockGameCache, routeParams, mockGameDetails;
    var mockGameActions;

    beforeEach(inject(function (_$rootScope_, $httpBackend, _$q_, _$controller_, _$timeout_) {
        $rootScope = _$rootScope_;
        $http = $httpBackend;
        controller = _$controller_;
        $q = _$q_;
        $timeout = _$timeout_;
        spyOn($rootScope, '$broadcast').and.callThrough();
        gameDisplay = jasmine.createSpyObj('gameDisplay', ['initializeScope', 'processGameUpdateForScope', 'updateScopeForGame']);
        $location = {path: jasmine.createSpy()};
        ads = {
            showAdPopup: function () {
                adPopupModalResult = $q.defer();
                adsCalled = true;
                return adPopupModalResult.promise;
            }
        };
        adsCalled = false;
        mockGameActions = jasmine.createSpyObj('gameActions', ['quit', 'reject', 'accept', 'reject', 'rematch', 'wrapActionOnGame']);
        var playerUrl = 'http://xx.com/api/player/MANUAL1';
        mockGameActions.getGameURL = function (game) {
            return playerUrl + '/game/' + game.id + '/';
        };

        $scope = $rootScope.$new();

        mockPlayerService = {
            currentPlayerBaseURL: function () {
                return playerUrl;
            },
            currentPlayer: function () {
                return player;
            }
        };

        mockGameCache = {
            getGameForID: function (id) {
                expect(id).toEqual(gameCacheExpectedId);
                return gameCacheReturnResult;
            }
        };

        mockGameDetails = {};

        routeParams = {
            gameID: 'gameid'
        };


    }));

    describe('good initialization', function () {
        var element;
        beforeEach(inject(function ($document) {
            gameCacheExpectedId = 'gameid';
            gameCacheReturnResult = game;

            element = angular.element('<div class="x" id="game-wrapper"></div>');
            spyOn(element[0], 'focus');
            angular.element($document).find('body').append(element);
            $rootScope.$digest();

            ctrl = controller('ShowCtrl', {
                $routeParams: routeParams,
                $rootScope: $rootScope,
                $scope: $scope,
                $location: $location,
                jtbPlayerService: mockPlayerService,
                twGameDisplay: gameDisplay,
                jtbGameCache: mockGameCache,
                twGameDetails: mockGameDetails,
                jtbBootstrapGameActions: mockGameActions,
                twAds: ads
            });
        }));

        it('initializes', function () {
            expect(gameDisplay.initializeScope).toHaveBeenCalledWith($scope);

            expect($scope.player).toEqual(player);
            expect(gameDisplay.updateScopeForGame).toHaveBeenCalledWith($scope, game);
            expect($scope.gameDetails).toBe(mockGameDetails);
            expect(adsCalled).toEqual(false);
            expect(element[0].focus).not.toHaveBeenCalled();
            $timeout.flush();
            expect(element[0].focus).toHaveBeenCalled();
        });
    });

    describe('bad initialization', function () {
        beforeEach(function () {
            gameCacheExpectedId = 'gameid';
            gameCacheReturnResult = undefined;

            ctrl = controller('ShowCtrl', {
                $routeParams: routeParams,
                $rootScope: $rootScope,
                $scope: $scope,
                $location: $location,
                $window: window,
                jtbPlayerService: mockPlayerService,
                twGameDisplay: gameDisplay,
                jtbGameCache: mockGameCache,
                jtbBootstrapGameActions: mockGameActions,
                twGameDetails: mockGameDetails,
                twAds: ads
            });
        });

        it('initializes with no game', function () {
            expect($scope.gameDetails).toBe(mockGameDetails);
            expect(gameDisplay.initializeScope).toHaveBeenCalledWith($scope);
            expect($scope.player).toEqual(player);
            expect($scope.game).toBeUndefined();
            expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith($scope, game);
            expect(adsCalled).toEqual(false);
        });
    });

    describe('post initialization tests', function () {
        beforeEach(function () {
            gameCacheExpectedId = 'gameid';
            gameCacheReturnResult = game;

            ctrl = controller('ShowCtrl', {
                $routeParams: routeParams,
                $rootScope: $rootScope,
                $scope: $scope,
                $location: $location,
                $window: window,
                jtbPlayerService: mockPlayerService,
                jtbBootstrapGameActions: mockGameActions,
                twGameDisplay: gameDisplay,
                jtbGameCache: mockGameCache,
                twGameDetails: mockGameDetails,
                twAds: ads
            });
        });

        describe('listens for gameUpdate broadcasts', function () {
            it('listens for gameUpdate and updates if same game id', function () {
                $scope.game = game;
                var gameUpdate = angular.copy(game);
                $rootScope.$broadcast('gameUpdated', game, gameUpdate);
                $rootScope.$apply();
                expect(gameDisplay.updateScopeForGame).toHaveBeenCalledWith($scope, gameUpdate);
                expect(adsCalled).toEqual(false);
            });

            it('listens for gameUpdate and ignores if different game id', function () {
                $scope.game = game;
                var gameUpdate = angular.copy(game);
                gameUpdate.id = 'notid';
                $rootScope.$broadcast('gameUpdated', gameUpdate, gameUpdate);
                $rootScope.$apply();
                expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith($scope, gameUpdate);
                expect(adsCalled).toEqual(false);
            });

            it('listens for gameUpdate and ignores if scope has no game', function () {
                var gameUpdate = angular.copy(game);
                gameUpdate.id = 'notid';
                $rootScope.$broadcast('gameUpdated', game, gameUpdate);
                $rootScope.$apply();
                expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith($scope, gameUpdate);
                expect(adsCalled).toEqual(false);
            });
        });

        describe('listens for gameCachesLoaded broadcasts', function () {
            it('listens for gameCachesLoaded and updates from game', function () {
                $scope.game = game;
                var gameUpdate = angular.copy(game);
                gameCacheReturnResult = gameUpdate;
                $rootScope.$broadcast('gameCachesLoaded');
                $rootScope.$apply();
                expect(gameDisplay.updateScopeForGame).toHaveBeenCalledWith($scope, gameUpdate);
                expect(adsCalled).toEqual(false);
            });

            it('listens for gameCachesLoaded and goes to main page if no longer valid', function () {
                $scope.game = game;
                var gameUpdate;
                gameCacheReturnResult = gameUpdate;
                $rootScope.$broadcast('gameCachesLoaded');
                $rootScope.$apply();
                expect(gameDisplay.updateScopeForGame).not.toHaveBeenCalledWith($scope, gameUpdate);
                expect($location.path).toHaveBeenCalledWith('/');
                expect(adsCalled).toEqual(false);
            });
        });

        describe('test various action processing', function () {
            it('post rematch', function () {
                $scope.startNextRound();
                adPopupModalResult.resolve();
                $rootScope.$apply();

                expect(mockGameActions.rematch).toHaveBeenCalledWith(game);
            });

            it('accept match', function () {
                $scope.accept();
                adPopupModalResult.resolve();
                $rootScope.$apply();

                expect(mockGameActions.accept).toHaveBeenCalledWith(game);
            });

            it('reject match', function () {
                $scope.reject();
                expect(mockGameActions.reject).toHaveBeenCalledWith(game);
            });

            it('quit match', function () {
                $scope.quit();

                expect(mockGameActions.quit).toHaveBeenCalledWith(game);
                expect(adsCalled).toEqual(false);
            });

            it('set puzzle', function () {
                $scope.enteredCategory = 'cat';
                $scope.enteredWordPhrase = 'wp';

                $http.expectPUT('http://xx.com/api/player/MANUAL1/game/gameid/puzzle', {
                    category: 'cat',
                    wordPhrase: 'wp'
                }).respond(200);
                $scope.setPuzzle();
                $http.flush();

                expect(mockGameActions.wrapActionOnGame).toHaveBeenCalled();
                expect(mockGameActions.wrapActionOnGame.calls.mostRecent().args[0]).toBeDefined();
            });

            it('guess letter', function () {
                $scope.allowPlayMoves = true;
                $http.expectPUT('http://xx.com/api/player/MANUAL1/game/gameid/guess/a').respond(200);
                $scope.sendGuess('a');
                $http.flush();

                expect(mockGameActions.wrapActionOnGame).toHaveBeenCalled();
                expect(mockGameActions.wrapActionOnGame.calls.mostRecent().args[0]).toBeDefined();
            });

            it('guess letter via keypress', function () {
                $scope.allowPlayMoves = true;
                $http.expectPUT('http://xx.com/api/player/MANUAL1/game/gameid/guess/a').respond(200);
                $scope.onKeyPress({key: 'a'});
                $http.flush();

                expect(mockGameActions.wrapActionOnGame).toHaveBeenCalled();
                expect(mockGameActions.wrapActionOnGame.calls.mostRecent().args[0]).toBeDefined();
            });

            it('steal letter', function () {
                $scope.allowPlayMoves = true;
                $http.expectPUT('http://xx.com/api/player/MANUAL1/game/id/steal/2').respond(200);
                $scope.stealLetter('2');
                $http.flush();

                expect(mockGameActions.wrapActionOnGame).toHaveBeenCalled();
                expect(mockGameActions.wrapActionOnGame.calls.mostRecent().args[0]).toBeDefined();
            });
        });
    });
});
