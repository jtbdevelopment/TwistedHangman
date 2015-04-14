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

    var ctrl, scope, rootScope, phaseService, q, phaseDeferred, location, gameCache, timeout, animate;
    var phaseDesc = {};
    phaseDesc[phase] = ['unused', 'desc'];
    var games;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope, $q, $timeout, $animate) {
      scope = $rootScope.$new();
      timeout = $timeout;
      location = {path: jasmine.createSpy()};
      rootScope = $rootScope;
      animate = $animate;
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
        jtbGamePhaseService: phaseService,
        jtbGameCache: gameCache
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
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(games);
        var newStatus = [{'Y': 2}, {'Z': 5}];
        games.splice(0);
        newStatus.forEach(function (status) {
          games.push(status);
        });
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(newStatus);
      });
    });

    describe('show hide click function', function () {
      it('initial value and switching', function () {
        expect(scope.hideGames).toEqual(false);
        scope.switchHideGames();
        expect(scope.hideGames).toEqual(true);
        scope.switchHideGames();
        expect(scope.hideGames).toEqual(false);
      });
    });

    describe('broadcast phase changes', function () {
      var element, animation, promise, removed, added, elementCalled;
      beforeEach(function () {
        element = {};
        animation = 'animated shake';
        removed = false;
        added = false;
        elementCalled = false;
        promise = undefined;

        spyOn(angular, 'element').and.callFake(function () {
          elementCalled = true;
          return element;
        });
        spyOn(animate, 'addClass').and.callFake(function (elem, classes) {
          expect(elem).toBe(element);
          expect(classes).toEqual(animation);
          added = true;
          promise = q.defer();
          return promise.promise;
        });
        spyOn(animate, 'removeClass').and.callFake(function (elem, classes) {
          expect(elem).toBe(element);
          expect(classes).toEqual(animation);
          removed = true;
        });
      });

      it('adds shake animation when it can', function () {
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(games);
        rootScope.$broadcast('phaseChangeAlert', {id: 'game', gamePhase: phase});
        timeout.flush();
        expect(elementCalled).toEqual(true);
        expect(added).toEqual(true);
        promise.resolve({});
        rootScope.$apply();
        expect(removed).toEqual(true);
      });

      it('does not shake on wrong phase', function () {
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(games);
        rootScope.$broadcast('phaseChangeAlert', {id: 'game', gamePhase: phase + 'X'});
        timeout.flush();
        expect(elementCalled).toEqual(false);
        expect(promise).toBeUndefined();
        expect(added).toEqual(false);
        expect(removed).toEqual(false);
      });

      it('does not shake on no element', function () {
        element = undefined;
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(games);
        rootScope.$broadcast('phaseChangeAlert', {id: 'game', gamePhase: phase});
        timeout.flush();
        expect(elementCalled).toEqual(true);
        expect(promise).toBeUndefined();
        expect(added).toEqual(false);
        expect(removed).toEqual(false);
      });

      it('does not shake on no element', function () {
        element = undefined;
        rootScope.$broadcast('gameCachesLoaded');
        expect(scope.games).toEqual(games);
        rootScope.$broadcast('phaseChangeAlert', {id: 'game', gamePhase: phase});
        timeout.flush();
        expect(elementCalled).toEqual(true);
        expect(promise).toBeUndefined();
        expect(added).toEqual(false);
        expect(removed).toEqual(false);
      });
    });
  });
});


