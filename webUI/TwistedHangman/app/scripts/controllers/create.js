'use strict';

angular.module('twistedHangmanApp').controller('CreateCtrl',
    ['$rootScope', '$scope', 'jtbBootstrapGameActions', 'jtbAppLongName',
        'twGameFeatureService', 'jtbPlayerService', '$uibModal', 'twAds',
        function ($rootScope, $scope, jtbBootstrapGameActions, jtbAppLongName,
                  twGameFeatureService, jtbPlayerService, $uibModal, twAds) {

            var SINGLE_PLAYER = 'SinglePlayer';
            var TWO_PLAYERS = 'TwoPlayer';
            var MULTI_PLAYER = 'ThreePlus';
            var SYSTEM_PUZZLES = 'SystemPuzzles';

            function calcSubmitEnabled() {
                switch ($scope.desiredPlayerCount) {
                    case    SINGLE_PLAYER:
                        $scope.submitEnabled = ($scope.chosenFriends.length === 0);
                        break;
                    case    TWO_PLAYERS:
                        $scope.submitEnabled = ($scope.chosenFriends.length === 1);
                        break;
                    case    MULTI_PLAYER:
                        $scope.submitEnabled = ($scope.chosenFriends.length > 1);
                        break;
                }
            }

            $scope.featureData = {};
            twGameFeatureService.features().then(function (data) {
                $scope.featureData = data;
            });

            $scope.friends = [];
            $scope.invitableFBFriends = [];
            $scope.chosenFriends = [];
            jtbPlayerService.currentPlayerFriends().then(function (data) {
                angular.forEach(data.maskedFriends, function (displayName, hash) {
                    var friend = {
                        md5: hash,
                        displayName: displayName
                    };
                    $scope.friends.push(friend);
                });
                if (jtbPlayerService.currentPlayer().source === 'facebook') {
                    angular.forEach(data.invitableFriends, function (friend) {
                        var invite = {
                            id: friend.id,
                            name: friend.name
                        };
                        if (angular.isDefined(friend.picture) && angular.isDefined(friend.picture.url)) {
                            invite.url = friend.picture.url;
                        }
                        $scope.invitableFBFriends.push(invite);
                    });
                }
            });

            $scope.thieving = 'Thieving';
            $scope.drawGallows = '';
            $scope.drawFace = 'DrawFace';
            $scope.gamePace = 'Live';
            $scope.submitEnabled = false;
            $scope.desiredPlayerCount = '';
            $scope.playersEnabled = false;
            $scope.$watchCollection('chosenFriends', calcSubmitEnabled);

            $scope.clearPlayers = function () {
                $scope.chosenFriends = [];
                calcSubmitEnabled();
            };

            $scope.setSinglePlayer = function () {
                $scope.playersEnabled = false;
                $scope.desiredPlayerCount = SINGLE_PLAYER;
                $scope.gamePace = 'Live';
                $scope.wordPhraseSetter = SYSTEM_PUZZLES;
                $scope.winners = 'SingleWinner';
                $scope.h2hEnabled = false;
                $scope.alternatingEnabled = false;
                $scope.allFinishedEnabled = false;
                $scope.turnBasedEnabled = false;
                $scope.chosenFriends = [];
                calcSubmitEnabled();
            };

            $scope.setTwoPlayers = function () {
                $scope.playersEnabled = true;
                $scope.desiredPlayerCount = TWO_PLAYERS;
                $scope.h2hEnabled = true;
                $scope.alternatingEnabled = true;
                $scope.allFinishedEnabled = true;
                $scope.turnBasedEnabled = true;
                if ($scope.chosenFriends.length > 1) {
                    $scope.chosenFriends = [];
                }
                calcSubmitEnabled();
            };

            $scope.setThreePlayers = function () {
                $scope.playersEnabled = true;
                $scope.desiredPlayerCount = MULTI_PLAYER;
                $scope.h2hEnabled = false;
                $scope.alternatingEnabled = true;
                $scope.allFinishedEnabled = true;
                $scope.turnBasedEnabled = true;
                if ($scope.wordPhraseSetter === 'Head2Head') {
                    $scope.wordPhraseSetter = SYSTEM_PUZZLES;
                }
                calcSubmitEnabled();
            };

            $scope.createGame = function () {
                twAds.showAdPopup().then(function () {
                    var featureNames = ['wordPhraseSetter', 'desiredPlayerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace', 'winners'];
                    var featureSet = [];
                    featureSet = featureSet.concat(featureNames.map(function (name) {
                            var data = $scope[name];
                            if ((angular.isDefined(data)) && (data !== '')) {
                                return data;
                            }
                            return '';
                        }
                    ).filter(function (item) {
                        return item !== '';
                    }));

                    var players = $scope.chosenFriends.map(function (player) {
                        return player.md5;
                    });
                    var playersAndFeatures = {'players': players, 'features': featureSet};
                    jtbBootstrapGameActions.new(playersAndFeatures);
                });
            };

            $scope.showInvite = function () {
                $uibModal.open({
                    templateUrl: 'views/core-bs/friends/invite-friends.html',
                    controller: 'CoreBootstrapInviteCtrl',
                    controllerAs: 'invite',
                    size: 'lg',
                    resolve: {
                        invitableFriends: function () {
                            return $scope.invitableFBFriends;
                        },
                        message: function () {
                            return 'Come play ' + jtbAppLongName + ' with me!';
                        }
                    }
                });
            };

            //  Initialize
            $scope.setSinglePlayer();
        }
    ]
);
