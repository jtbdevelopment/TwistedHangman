'use strict';

describe('Service: twGameDetails', function () {
    beforeEach(module('twistedHangmanApp'));

    var game;
    var gameBase = {
        id: 'id',
        players: {'md1': 'P1', 'md2': 'P2', 'md3': 'P3', 'md4': 'P4', 'md5': 'P5'},
        wordPhraseSetter: 'md4',
        solverStates: {
            md1: {isPuzzleOver: true, isPuzzleSolved: true, field: 'X'},
            md2: {isPuzzleOver: true, isPuzzleSolved: false, field: 'Y'},
            md3: {isPuzzleOver: false, isPuzzleSolved: false, field: 'Z'}
        },
        playerStates: {
            md1: 'Pending',
            md2: 'Accepted',
            md3: 'Declined',
            md4: 'Quit',
            md5: 'Accepted',
            md6: 'Rejected'
        },
        playerImages: {
            md1: 'someimagelink',
            md2: 'anotherlink'
        },
        playerProfiles: {
            md1: 'someprofile',
            md2: 'anotherprofile'
        },
        featureData: {},
        features: [],
        playerRunningScores: {'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5},
        playerRoundScores: {'md1': 1, 'md2': 0, 'md3': -1, 'md4': 3, 'md5': 2}
    };

    var undef;
    var service;

    beforeEach(inject(function ($injector) {
        game = angular.copy(gameBase);
        service = $injector.get('twGameDetails');
    }));


    describe('test various state computation functions', function () {

        it('player response needed for undefined ', function () {
            expect(service.playerChallengeResponseNeeded(undef, 'md4')).toEqual(false);
            expect(service.playerChallengeResponseNeeded(game, undef)).toEqual(false);
            expect(service.playerChallengeResponseNeeded(game, ' ')).toEqual(false);
        });

        it('player icon based on state', function () {
            expect(service.stateIconForPlayer(game, 'md1')).toEqual('inbox');
            expect(service.stateIconForPlayer(game, 'md2')).toEqual('thumbs-up');
            expect(service.stateIconForPlayer(game, 'md3')).toEqual('question-sign');
            expect(service.stateIconForPlayer(game, 'md4')).toEqual('flag');
            expect(service.stateIconForPlayer(game, 'md5')).toEqual('thumbs-up');
            expect(service.stateIconForPlayer(game, 'md6')).toEqual('thumbs-down');
            expect(service.stateIconForPlayer(undef, 'md4')).toEqual('question-sign');
            expect(service.stateIconForPlayer(game, undef)).toEqual('question-sign');
            expect(service.stateIconForPlayer(game, ' ')).toEqual('question-sign');
        });

        it('player profile', function () {
            expect(service.profileForPlayer(game, 'md1')).toEqual('someprofile');
            expect(service.profileForPlayer(game, 'md2')).toEqual('anotherprofile');
            expect(service.profileForPlayer(game, 'md3')).toEqual('');
            expect(service.profileForPlayer(game, 'md4')).toEqual('');
            expect(service.profileForPlayer(game, 'md5')).toEqual('');
            expect(service.profileForPlayer(undef, 'md4')).toEqual('');
            expect(service.profileForPlayer(game, undef)).toEqual('');
            expect(service.profileForPlayer(game, ' ')).toEqual('');
        });

        it('player image', function () {
            expect(service.imageForPlayer(game, 'md1')).toEqual('someimagelink');
            expect(service.imageForPlayer(game, 'md2')).toEqual('anotherlink');
            expect(service.imageForPlayer(game, 'md3')).toEqual(null);
            expect(service.imageForPlayer(game, 'md4')).toEqual(null);
            expect(service.imageForPlayer(game, 'md5')).toEqual(null);
            expect(service.imageForPlayer(undef, 'md4')).toEqual(null);
            expect(service.imageForPlayer(game, undef)).toEqual(null);
            expect(service.imageForPlayer(game, ' ')).toEqual(null);
        });

        it('player response needed for game not in challenged ', function () {
            ['Setup', 'Playing', 'Quit', 'NextRoundStarted', 'RoundOver'].forEach(function (phase) {
                game.gamePhase = phase;
                ['md1', 'md2', 'md3', 'md4', 'md5'].forEach(function (md) {
                    expect(service.playerChallengeResponseNeeded(game, md)).toEqual(false);
                });
            });
        });

        it('player response needed for game in challenged ', function () {
            game.gamePhase = 'Challenged';
            expect(service.playerChallengeResponseNeeded(game, 'md1')).toEqual(true);
            expect(service.playerActionRequired(game, 'md1')).toEqual(true);
            ['md2', 'md3', 'md4', 'md5'].forEach(function (md) {
                expect(service.playerChallengeResponseNeeded(game, md)).toEqual(false);
                expect(service.playerActionRequired(game, md)).toEqual(false);
            });
        });

        it('player can play for undefined ', function () {
            expect(service.playerCanPlay(undef, 'md4')).toEqual(false);
            expect(service.playerCanPlay(game, undef)).toEqual(false);
            expect(service.playerCanPlay(game, ' ')).toEqual(false);
        });

        it('player can play for game not in Play ', function () {
            ['Setup', 'Challenged', 'Quit', 'NextRoundStarted', 'RoundOver'].forEach(function (phase) {
                game.gamePhase = phase;
                ['md1', 'md2', 'md3', 'md4', 'md5'].forEach(function (md) {
                    expect(service.playerCanPlay(game, md)).toEqual(false);
                });
            });
        });

        it('player can play for live game ', function () {
            game.gamePhase = 'Playing';
            game.wordPhraseSetter = 'md1';
            game.features = [];
            expect(service.playerCanPlay(game, 'md1', 'md2', 'md4', 'md5')).toEqual(false);
            ['md3'].forEach(function (md) {
                expect(service.playerCanPlay(game, md)).toEqual(true);
                expect(service.playerActionRequired(game, md)).toEqual(true);
            });
        });

        it('player can play for turn based game ', function () {
            game.gamePhase = 'Playing';
            game.wordPhraseSetter = 'md1';
            game.features = ['TurnBased'];
            game.featureData = {TurnBased: 'md3'};
            expect(service.playerCanPlay(game, 'md1')).toEqual(false);
            expect(service.playerCanPlay(game, 'md3')).toEqual(true);
            expect(service.playerActionRequired(game, 'md1')).toEqual(false);
            expect(service.playerActionRequired(game, 'md3')).toEqual(true);
            ['md2', 'md4', 'md5'].forEach(function (md) {
                expect(service.playerCanPlay(game, md)).toEqual(false);
                expect(service.playerActionRequired(game, md)).toEqual(false);
            });
        });

        it('player setup needed for undefined ', function () {
            expect(service.playerSetupEntryRequired(undef, 'md4')).toEqual(false);
            expect(service.playerSetupEntryRequired(game, undef)).toEqual(false);
            expect(service.playerSetupEntryRequired(game, ' ')).toEqual(false);
        });

        it('player setup needed for game not in Setup ', function () {
            ['Playing', 'Challenged', 'Quit', 'NextRoundStarted', 'RoundOver'].forEach(function (phase) {
                game.gamePhase = phase;
                ['md1', 'md2', 'md3', 'md4', 'md5'].forEach(function (md) {
                    expect(service.playerSetupEntryRequired(game, md)).toEqual(false);
                });
            });
        });

        it('player setup needed system challenge mode ', function () {
            game.gamePhase = 'Setup';
            game.wordPhraseSetter = '';
            ['md1', 'md2', 'md3', 'md4', 'md5'].forEach(function (md) {
                expect(service.playerSetupEntryRequired(game, md)).toEqual(false);
                expect(service.playerActionRequired(game, md)).toEqual(false);
            });
        });

        it('player setup for player challenge mode ', function () {
            game.gamePhase = 'Setup';
            game.wordPhraseSetter = 'md1';
            expect(service.playerSetupEntryRequired(game, 'md1')).toEqual(true);
            expect(service.playerActionRequired(game, 'md1')).toEqual(true);
            ['md2', 'md3', 'md4', 'md5'].forEach(function (md) {
                expect(service.playerSetupEntryRequired(game, md)).toEqual(false);
                expect(service.playerActionRequired(game, md)).toEqual(false);
            });
        });

        it('player setup for player h2h mode ', function () {
            game.gamePhase = 'Setup';
            game.wordPhraseSetter = null;
            game.players = {'md1': 'P1', 'md2': 'P2'};
            game.solverStates = {
                md1: {isPuzzleOver: true, isPuzzleSolved: true, field: 'X', wordPhrase: ''},
                md2: {isPuzzleOver: true, isPuzzleSolved: false, field: 'Y', wordPhrase: 'TEST'}
            };

            expect(service.playerSetupEntryRequired(game, 'md1')).toEqual(false);
            expect(service.playerActionRequired(game, 'md1')).toEqual(false);
            expect(service.playerSetupEntryRequired(game, 'md2')).toEqual(true);
            expect(service.playerActionRequired(game, 'md2')).toEqual(true);
            expect(service.playerSetupEntryRequired(game, 'md3')).toEqual(false);
            expect(service.playerActionRequired(game, 'md3')).toEqual(false);
        });

        it('role for player', function () {
            expect(service.roleForPlayer(game, 'md4')).toEqual('Set Puzzle');
            expect(service.playerIsSetter(game, 'md4')).toEqual(true);
            ['md1', 'md2', 'md3', 'md5'].forEach(function (md) {
                expect(service.roleForPlayer(game, md)).toEqual('Solver');
                expect(service.playerIsSetter(game, md)).toEqual(false);
            });
        });

        it('roleIcon for player', function () {
            expect(service.roleIconForPlayer(game, 'md4')).toEqual('eye-open');
            ['md1', 'md2', 'md3', 'md5'].forEach(function (md) {
                expect(service.roleIconForPlayer(game, md)).toEqual('pencil');
            });
        });

        it('role for player with undefined game/player', function () {
            expect(service.roleForPlayer(undef, 'md4')).toEqual('');
            expect(service.roleForPlayer(game, undef)).toEqual('');
            expect(service.roleForPlayer(game, ' ')).toEqual('');
            expect(service.playerIsSetter(undef, 'md4')).toEqual(false);
            expect(service.playerIsSetter(game, undef)).toEqual(false);
            expect(service.playerIsSetter(game, ' ')).toEqual(false);
        });

        it('roleIcon for player with undefined game/player', function () {
            expect(service.roleIconForPlayer(undef, 'md4')).toEqual('question-sign');
            expect(service.roleIconForPlayer(game, undef)).toEqual('question-sign');
            expect(service.roleIconForPlayer(game, ' ')).toEqual('question-sign');
        });

        it('gameEndForPlayer for player', function () {
            expect(service.gameEndForPlayer(game, 'md1')).toEqual('Solved!');
            expect(service.gameEndForPlayer(game, 'md2')).toEqual('Hung!');
            expect(service.gameEndForPlayer(game, 'md3')).toEqual('Not Solved.');
            expect(service.gameEndForPlayer(game, 'md4')).toEqual('N/A');
            expect(service.gameEndForPlayer(game, 'md5')).toEqual('Unknown');
        });
        it('gameStateIconForPlayer for player', function () {
            expect(service.gameStateIconForPlayer(game, 'md1')).toEqual('ok');
            expect(service.gameStateIconForPlayer(game, 'md2')).toEqual('remove');
            expect(service.gameStateIconForPlayer(game, 'md3')).toEqual('search');
            expect(service.gameStateIconForPlayer(game, 'md4')).toEqual('question-sign');
            expect(service.gameStateIconForPlayer(game, 'md5')).toEqual('question-sign');
        });

        it('gameEndForPlayer with undefined game', function () {
            expect(service.gameEndForPlayer(undef, 'md4')).toEqual('');
            expect(service.gameEndForPlayer(game, undef)).toEqual('');
            expect(service.gameEndForPlayer(game, ' ')).toEqual('');
        });

        it('gameStateIconForPlayer with undefined game', function () {
            expect(service.gameStateIconForPlayer(undef, 'md4')).toEqual('question-sign');
            expect(service.gameStateIconForPlayer(game, undef)).toEqual('question-sign');
            expect(service.gameStateIconForPlayer(game, ' ')).toEqual('question-sign');
        });

        it('stateForPlayer for player', function () {
            expect(service.stateForPlayer(game, 'md1', 'field')).toEqual('X');
            expect(service.stateForPlayer(game, 'md2', 'field')).toEqual('Y');
            expect(service.stateForPlayer(game, 'md3', 'field')).toEqual('Z');
            expect(service.stateForPlayer(game, 'md4', 'field')).toEqual('N/A');
            expect(service.stateForPlayer(game, 'md5', 'field')).toEqual('Unknown');
        });

        it('stateForPlayer with undefined game/player', function () {
            expect(service.stateForPlayer(undef, 'md4')).toEqual('');
            expect(service.stateForPlayer(game, undef)).toEqual('');
            expect(service.stateForPlayer(game, ' ')).toEqual('');
        });

        it('gameScoreForPlayer with undefined game/player', function () {
            expect(service.gameScoreForPlayer(undef, 'md4')).toEqual('');
            expect(service.gameScoreForPlayer(game, undef)).toEqual('');
            expect(service.gameScoreForPlayer(game, '  ')).toEqual('');
        });

        it('gameScoreForPlayer for player', function () {
            angular.forEach({'md1': 1, 'md2': 0, 'md3': -1, 'md4': 3, 'md5': 2}, function (value, key) {
                expect(service.gameScoreForPlayer(game, key)).toEqual(value);
            });
        });

        it('runningScoreForPlayer with undefined game/player', function () {
            expect(service.runningScoreForPlayer(undef, 'md4')).toEqual('');
            expect(service.runningScoreForPlayer(game, undef)).toEqual('');
            expect(service.runningScoreForPlayer(game, '  ')).toEqual('');
        });

        it('runningScoreForPlayer for player', function () {
            angular.forEach({'md1': 0, 'md2': 2, 'md3': 10, 'md4': -3, 'md5': -5}, function (value, key) {
                expect(service.runningScoreForPlayer(game, key)).toEqual(value);
            });
        });
    });

    describe('testing the description function', function () {
        it('undef', function () {
            game.features = ['Thieving', 'TurnBased', 'SystemPuzzles', 'SinglePlayer', 'SingleWinner'];
            game.featureData.TurnBased = 'md2';
            expect(service.gameDescription(undefined)).toEqual('');
            expect(service.shortGameDescription(undefined)).toEqual('');
        });

        it('1', function () {
            game.features = ['Thieving', 'TurnBased', 'SystemPuzzles', 'SinglePlayer', 'SingleWinner'];
            game.featureData.TurnBased = 'md2';
            expect(service.gameDescription(game)).toEqual('Thieving Allowed, Generated Puzzle, P2\'s Turn');
            expect(service.shortGameDescription(game)).toEqual('Thieving, Generated, Turns');
        });

        it('2', function () {
            game.features = ['AlternatingPuzzleSetter', 'TwoPlayer', 'SingleWinner'];
            game.featureData.TurnBased = 'md2';
            expect(service.gameDescription(game)).toEqual('Puzzle Set By P4, Until First Solver, Live Play');
            expect(service.shortGameDescription(game)).toEqual('Player Puzzle, 1st Solver, Live');
        });

        it('3', function () {
            game.features = [];
            game.featureData.TurnBased = 'md2';
            expect(service.gameDescription(game)).toEqual('Head-2-Head Puzzles, Until All Finish, Live Play');
            expect(service.shortGameDescription(game)).toEqual('Head-2-Head, All Finish, Live');
        });
    });


});
