'use strict';

angular.module('twistedHangmanApp').controller('ShowCtrl',
    ['$scope', '$routeParams', '$http', '$location', 'jtbBootstrapGameActions',
        'jtbPlayerService', 'twGameDisplay', 'jtbGameCache', 'twGameDetails', 'twAds',
        function ($scope, $routeParams, $http, $location, jtbBootstrapGameActions,
                  jtbPlayerService, twGameDisplay, jtbGameCache, twGameDetails, twAds) {
            twGameDisplay.initializeScope($scope);
            $scope.gameID = $routeParams.gameID;
            $scope.enteredCategory = '';
            $scope.enteredWordPhrase = '';
            $scope.gameDetails = twGameDetails;
            $scope.player = jtbPlayerService.currentPlayer();
            var game = jtbGameCache.getGameForID($scope.gameID);
            if (angular.isDefined(game)) {
                twGameDisplay.updateScopeForGame($scope, game);
            }

            $scope.$on('playerLoaded', function () {
                $location.path('/');
            });

            $scope.$on('gameCachesLoaded', function () {
                var game = jtbGameCache.getGameForID($scope.gameID);
                if (angular.isDefined(game)) {
                    twGameDisplay.updateScopeForGame($scope, jtbGameCache.getGameForID($scope.gameID));
                } else {
                    $location.path('/');
                }
            });

            $scope.$on('gameUpdated', function (event, oldGame, newGame) {
                if (angular.isDefined($scope.game) && $scope.game.id === newGame.id) {
                    twGameDisplay.processGameUpdateForScope($scope, newGame);
                }
            });

            $scope.startNextRound = function () {
                twAds.showAdPopup().then(function () {
                    jtbBootstrapGameActions.rematch(game);
                });
            };

            $scope.accept = function () {
                twAds.showAdPopup().then(function () {
                    jtbBootstrapGameActions.accept(game);
                });
            };

            $scope.reject = function () {
                jtbBootstrapGameActions.reject(game);
            };

            $scope.setPuzzle = function () {
                jtbBootstrapGameActions.wrapActionOnGame(
                    $http.put(
                        jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/puzzle',
                        {category: $scope.enteredCategory, wordPhrase: $scope.enteredWordPhrase}
                    )
                );
            };

            $scope.sendGuess = function (letter) {
                if ($scope.allowPlayMoves) {
                    jtbBootstrapGameActions.wrapActionOnGame(
                        $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/guess/' + letter)
                    );
                }
            };

            $scope.stealLetter = function (position) {
                if ($scope.allowPlayMoves) {
                    jtbBootstrapGameActions.wrapActionOnGame(
                        $http.put(jtbBootstrapGameActions.getGameURL(game) + 'steal/' + position)
                    );
                }
            };

            $scope.quit = function () {
                jtbBootstrapGameActions.quit(game);
            };
        }
    ])
;
