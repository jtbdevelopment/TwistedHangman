'use strict';

describe('Controller: MenuCtrl', function () {

    beforeEach(module('ngAnimateMock'));
    beforeEach(module('twistedHangmanApp'));

    var MenuCtrl, $scope, $rootScope, $timeout, $animate, $document;

    var games = {};
    var gameCache = {
        getGamesForPhase: function (phase) {
            return games[phase];
        }
    };

    var twGameDetails = {
        shortGameDescription: function (game) {
            return game.id + '/SD';
        }
    };
    var testPhasesAndIcons = {
        'Phase 1 Description': 'icon1',
        'Aha.': 'icon3',
        'Description Phase 2': 'icon2'
    };

    var jtbGameClassifier = {
        getIcons: function () {
            return testPhasesAndIcons;
        },
        getClassifications: function () {
            var c = [];
            angular.forEach(testPhasesAndIcons, function (value, key) {
                c.push(key);
            });
            return c;
        }
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, _$rootScope_, _$document_, _$timeout_, _$animate_) {
        $scope = _$rootScope_.$new();
        $rootScope = _$rootScope_;
        $timeout = _$timeout_;
        $animate = _$animate_;
        $document = _$document_;
        MenuCtrl = $controller('MenuCtrl', {
            $scope: $scope,
            jtbGameClassifier: jtbGameClassifier,
            twGameDetails: twGameDetails,
            jtbGameCache: gameCache
        });
    }));

    it('initializes', function () {
        expect(MenuCtrl.phases).toEqual(['Phase 1 Description', 'Aha.', 'Description Phase 2']);
        expect(MenuCtrl.phaseLabels).toEqual({
            'Phase 1 Description': 'Phase 1 Description',
            'Aha.': 'Aha.',
            'Description Phase 2': 'Description Phase 2'
        });
        expect(MenuCtrl.phaseDescriptions).toEqual({
            'Phase 1 Description': 'Phase 1 Description',
            'Aha.': 'Aha.',
            'Description Phase 2': 'Description Phase 2'
        });

        expect(MenuCtrl.phaseStyles).toEqual({
            'Phase 1 Description': 'phase-1-description',
            'Aha.': 'aha',
            'Description Phase 2': 'description-phase-2'
        });
        expect(MenuCtrl.phaseCollapsed).toEqual({
            'Phase 1 Description': false,
            'Aha.': false,
            'Description Phase 2': false
        });
        expect(MenuCtrl.games).toEqual({
            'Phase 1 Description': [],
            'Aha.': [],
            'Description Phase 2': []
        });
        expect(MenuCtrl.phaseGlyphicons).toEqual({
            'Phase 1 Description': 'icon1',
            'Aha.': 'icon3',
            'Description Phase 2': 'icon2'
        });
    });

    describe('updates games from cache on various broadcast messages', function () {
        angular.forEach(['gameCachesLoaded', 'gameRemoved', 'gameAdded', 'gameUpdated'], function (message) {
            games = {};
            angular.forEach(testPhasesAndIcons, function (value, key) {
                var gamesToCreate = Math.floor(Math.random() * 10);
                games[key] = [];
                for (var i = 0; i < gamesToCreate; ++i) {
                    games[key].push({id: Math.floor(Math.random() * 100000), gamePhase: 'X'});
                }
            });

            it('refreshes games on ' + message, function () {
                //  two junk params because of animation - animation tested later
                $rootScope.$broadcast(message, games['Phase 1 Description'][0], games['Phase 1 Description'][0]);
                $rootScope.$apply();
                expect(MenuCtrl.games).toEqual(games);
                angular.forEach(games, function (phase) {
                    angular.forEach(phase, function (game) {
                        expect(MenuCtrl.descriptions[game.id]).toEqual(game.id + '/SD');
                    });
                });
            });
        });
    });

    describe('animation', function () {
        var element;

        var game;
        var id = 1;
        beforeEach(function () {
            id += 1;
            game = {id: id, gamePhase: 'X'};
            element = angular.element('<div class="x" id="' + id + '">Changed</div>');
            angular.element($document).find('body').append(element);
            $rootScope.$digest();
            $animate.enabled(true);
        });

        it('shakes when game updated with phase change', function () {
            var newGame = {id: game.id, gamePhase: 'XX'};
            $rootScope.$broadcast('gameUpdated', game, newGame);
            $rootScope.$apply();

            expect(element.hasClass('animated')).toEqual(false);
            expect(element.hasClass('shake')).toEqual(false);
            expect(element.hasClass('x')).toEqual(true);

            $timeout.flush();
            $rootScope.$digest();

            expect(element.hasClass('animated')).toEqual(true);
            expect(element.hasClass('shake')).toEqual(true);
            expect(element.hasClass('x')).toEqual(true);

            $animate.flush();

            expect(element.hasClass('animated')).toEqual(false);
            expect(element.hasClass('shake')).toEqual(false);
            expect(element.hasClass('x')).toEqual(true);
        });

        it('shakes when added', function () {
            $rootScope.$broadcast('gameAdded', game);
            $rootScope.$apply();

            expect(element.hasClass('animated')).toEqual(false);
            expect(element.hasClass('shake')).toEqual(false);
            expect(element.hasClass('x')).toEqual(true);

            $timeout.flush();
            $rootScope.$digest();

            expect(element.hasClass('animated')).toEqual(true);
            expect(element.hasClass('shake')).toEqual(true);
            expect(element.hasClass('x')).toEqual(true);

            $animate.flush();

            expect(element.hasClass('animated')).toEqual(false);
            expect(element.hasClass('shake')).toEqual(false);
            expect(element.hasClass('x')).toEqual(true);
        });

        it('does not shakes when game updated with not phase change', function () {
            var newGame = angular.copy(game);
            newGame.something = 'change';
            $rootScope.$broadcast('gameUpdated', game, newGame);
            $rootScope.$apply();

            expect(element.hasClass('animated')).toEqual(false);
            expect(element.hasClass('shake')).toEqual(false);
            expect(element.hasClass('x')).toEqual(true);

            try {
                $timeout.flush();
            } catch (ex) {
                return;
            }
            expect(1).toEqual(2);
        });

        it('does not shake when game id is not found', function () {
            game.id = 100000;
            var newGame = angular.copy(game);
            newGame.something = 'change';
            $rootScope.$broadcast('gameUpdated', game, newGame);
            $rootScope.$apply();

            expect(element.hasClass('animated')).toEqual(false);
            expect(element.hasClass('shake')).toEqual(false);
            expect(element.hasClass('x')).toEqual(true);

            try {
                $timeout.flush();
            } catch (ex) {
                return;
            }
            expect(1).toEqual(2);
        });
    });
});
