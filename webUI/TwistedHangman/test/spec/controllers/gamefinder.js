'use strict';
var phasesAndSymbols = {
  Playing: 'play',
  Setup: 'comment',
  Challenged: 'inbox',
  RoundOver: 'repeat',
  Declined: 'remove',
  NextRoundStarted: 'ok-sign',
  Quit: 'flag'
};
angular.forEach(phasesAndSymbols, function (glyph, phase) {
  var name = phase + 'Games';
  var test = 'Controller: ' + name + 'Games';

  describe(test, function () {
    // load the controller's module
    beforeEach(module('twistedHangmanApp'));

    var ctrl, scope, rootScope, phaseService, q, phaseDeferred, location, gameCache;
    var phaseDesc = {};
    phaseDesc[phase] = ['unused', 'desc'];
    var games;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope, $q) {
      scope = $rootScope.$new();
      location = {path: jasmine.createSpy()};
      rootScope = $rootScope;
      q = $q;
      phaseService = {
        phases: function () {
          phaseDeferred = q.defer();
          return phaseDeferred.promise;
        }
      };
      gameCache = {
        getGamesForPhase: function (gamePhase) {
          expect(gamePhase).toEqual(phase);
          return games;
        }
      };

      ctrl = $controller(name, {
        $scope: scope,
        $location: location,
        twGamePhaseService: phaseService,
        twGameCache: gameCache
      });
    }));

    describe('error initialization tests', function () {
      it('goes to error page if phase errors', function () {
        phaseDeferred.reject();
        rootScope.$apply();
        expect(location.path).toHaveBeenCalledWith('/error');
      });
    });

    describe('normal initialization tests', function () {
      beforeEach(function () {
        phaseDeferred.resolve(phaseDesc);
        rootScope.$apply();
        games = [{'X': 1}, {'Y': 2}];
      });

      it('sets games to empty initially and then calls game cache', function () {
        expect(scope.label).toEqual('desc');
        expect(scope.glyph).toEqual('glyphicon-' + glyph);
        expect(scope.style).toEqual(phase.toLowerCase() + 'Button');
        expect(scope.games).toEqual([]);
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(games);
      });

      it('refreshes on "gameCachesLoaded" broadcast', function () {
        expect(scope.games).toEqual(games);
        var newStatus = [{'Y': 2}, {'Z': 5}];
        games = newStatus;
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(newStatus);
      });
    });
  });
});


