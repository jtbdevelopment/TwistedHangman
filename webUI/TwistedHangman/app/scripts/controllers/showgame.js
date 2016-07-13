'use strict';

//  TODO - should all the playing failures refresh game?
angular.module('twistedHangmanApp').controller('ShowCtrl',
    ['$scope', '$routeParams', '$http', '$location', '$uibModal',
        'jtbPlayerService', 'twGameDisplay', 'jtbGameCache', 'twGameDetails', 'twAds',
        function ($scope, $routeParams, $http, $location, $uibModal,
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

            $scope.$on('gameUpdate', function (event, id, game) {
                if (angular.isDefined($scope.game) && $scope.game.id === id) {
                    //  TODO - this generates a stale update warning as game cache is also listening
                    twGameDisplay.processGameUpdateForScope($scope, game);
                }
            });

            function showMessage(alertMessage) {
                $uibModal.open({
                    templateUrl: 'views/gameErrorDialog.html',
                    controller: 'ErrorCtrl',
                    resolve: {
                        message: function () {
                            return alertMessage;
                        }
                    }
                });
            }

            function showConfirmDialog() {
                return $uibModal.open({
                    templateUrl: 'views/confirmDialog.html',
                    controller: 'ConfirmCtrl'
                });
            }

            //  TODO - refresh game on error on action?

            $scope.startNextRound = function () {
                twAds.showAdPopup().result.then(function () {
                    $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/rematch').success(function (data) {
                        twGameDisplay.processGameUpdateForScope($scope, data);
                        $location.path('/show/' + data.id);
                    }).error(function (data, status, headers, config) {
                        showMessage(data);
                        console.error(data + status + headers + config);
                    });
                });
            };

            $scope.accept = function () {
                twAds.showAdPopup().result.then(function () {
                    $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/accept').success(function (data) {
                        twGameDisplay.processGameUpdateForScope($scope, data);
                    }).error(function (data, status, headers, config) {
                        showMessage(data);
                        console.error(data + status + headers + config);
                    });
                });
            };

            $scope.reject = function () {
                var modal = showConfirmDialog();
                modal.result.then(function () {
                    $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/reject').success(function (data) {
                        twGameDisplay.processGameUpdateForScope($scope, data);
                    }).error(function (data, status, headers, config) {
                        showMessage(data);
                        console.error(data + status + headers + config);
                    });
                });
            };

            $scope.setPuzzle = function () {
                $http.put(
                    jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/puzzle',
                    {
                        category: $scope.enteredCategory,
                        wordPhrase: $scope.enteredWordPhrase
                    }).success(function (data) {
                    twGameDisplay.processGameUpdateForScope($scope, data);
                }).error(function (data, status, headers, config) {
                    showMessage(data);
                    console.error(data + status + headers + config);
                });
            };

            $scope.sendGuess = function (letter) {
                if ($scope.allowPlayMoves) {
                    $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/guess/' + letter).success(function (data) {
                        twGameDisplay.processGameUpdateForScope($scope, data);
                    }).error(function (data, status, headers, config) {
                        showMessage(data);
                        console.error(data + status + headers + config);
                    });
                } else {
                    showMessage('Not currently playable.');
                }
            };

            $scope.stealLetter = function (position) {
                if ($scope.allowPlayMoves) {
                    $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/steal/' + position).success(function (data) {
                        twGameDisplay.processGameUpdateForScope($scope, data);
                    }).error(function (data, status, headers, config) {
                        showMessage(data);
                        console.error(data + status + headers + config);
                    });
                } else {
                    showMessage('Not currently playable.');
                }
            };

            $scope.quit = function () {
                var modal = showConfirmDialog();
                modal.result.then(function () {
                    $http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + $scope.gameID + '/quit').success(function (data) {
                        twGameDisplay.processGameUpdateForScope($scope, data);
                    }).error(function (data, status, headers, config) {
                        showMessage(data);
                        console.error(data + status + headers + config);
                    });
                });
            };
        }
    ])
;
