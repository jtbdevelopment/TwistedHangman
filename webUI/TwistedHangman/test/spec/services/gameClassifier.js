'use strict';

describe('Service: gameClassifier', function () {
    beforeEach(module('twistedHangmanApp'));

    var playerMD5 = 'anmd5!';
    var mockPlayerService = {
        currentPlayer: function () {
            return {md5: playerMD5};
        }
    };

    var twGameDetails = {
        playerActionRequired: jasmine.createSpy('action')
    };

    beforeEach(module(function ($provide) {
        $provide.factory('jtbPlayerService', [function () {
            return mockPlayerService;
        }]);
        $provide.factory('twGameDetails', [function () {
            return twGameDetails;
        }]);
    }));

    var service;
    beforeEach(inject(function ($injector) {
        twGameDetails.playerActionRequired.calls.reset();
        service = $injector.get('jtbGameClassifier');
    }));

    var expectedYourTurnClassification = 'Your move.';
    var expectedTheirTurnClassification = 'Their move.';
    var expectedOlderGameClassification = 'Older games.';
    var expectedIconMap = {};
    expectedIconMap[expectedYourTurnClassification] = 'play';
    expectedIconMap[expectedTheirTurnClassification] = 'pause';
    expectedIconMap[expectedOlderGameClassification] = 'stop';


    it('get classifications', function () {
        expect(service.getClassifications()).toEqual([expectedYourTurnClassification, expectedTheirTurnClassification, expectedOlderGameClassification]);
    });

    it('get icon map', function () {
        expect(service.getIcons()).toEqual(expectedIconMap);
    });

    it('classification for no player action needed, non-roundover phase', function () {
        var game = {gamePhase: 'TBD'};
        twGameDetails.playerActionRequired.and.returnValue(false);
        expect(service.getClassification(game)).toEqual(expectedTheirTurnClassification);
        expect(twGameDetails.playerActionRequired).toHaveBeenCalledWith(game, playerMD5);
    });

    it('classification for player action needed, non-roundover phase', function () {
        var game = {gamePhase: 'TBD'};
        twGameDetails.playerActionRequired.and.returnValue(true);
        expect(service.getClassification(game)).toEqual(expectedYourTurnClassification);
        expect(twGameDetails.playerActionRequired).toHaveBeenCalledWith(game, playerMD5);
    });

    it('classification for phase RoundOver', function () {
        var game = {gamePhase: 'RoundOver'};
        twGameDetails.playerActionRequired.and.returnValue(false);
        expect(service.getClassification(game)).toEqual(expectedYourTurnClassification);
        expect(twGameDetails.playerActionRequired).toHaveBeenCalledWith(game, playerMD5);
    });

    angular.forEach(['Declined', 'Quit', 'NextRoundStarted'], function (phase) {
        it('classification for phase ' + phase, function () {
            var game = {gamePhase: phase};
            twGameDetails.playerActionRequired.and.returnValue(false);
            expect(service.getClassification(game)).toEqual(expectedOlderGameClassification);
        });
    });
});