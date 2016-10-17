'use strict';

angular.module('twistedHangmanApp').controller('CreateCtrl',
    ['$rootScope', '$scope', 'jtbBootstrapGameActions', 'jtbAppLongName',
        'twGameFeatureService', 'jtbPlayerService', '$uibModal', 'twAds',
        function ($rootScope, $scope, jtbBootstrapGameActions, jtbAppLongName,
                  twGameFeatureService, jtbPlayerService, $uibModal, twAds) {

            var controller = this;
            var SINGLE_PLAYER = 'SinglePlayer';
            var TWO_PLAYERS = 'TwoPlayer';
            var MULTI_PLAYER = 'ThreePlus';
            var SYSTEM_PUZZLES = 'SystemPuzzles';

            function calcSubmitEnabled() {
                switch (controller.desiredPlayerCount) {
                    case    SINGLE_PLAYER:
                        controller.submitEnabled = (controller.chosenFriends.length === 0);
                        break;
                    case    TWO_PLAYERS:
                        controller.submitEnabled = (controller.chosenFriends.length === 1);
                        break;
                    case    MULTI_PLAYER:
                        controller.submitEnabled = (controller.chosenFriends.length > 1);
                        break;
                }
            }

            controller.featureData = {};
            twGameFeatureService.features().then(function (data) {
                controller.featureData = data;
            });

            jtbPlayerService.initializeFriendsForController(controller);

            controller.thieving = 'Thieving';
            controller.drawGallows = '';
            controller.drawFace = 'DrawFace';
            controller.gamePace = 'Live';
            controller.submitEnabled = false;
            controller.desiredPlayerCount = '';
            controller.playersEnabled = false;
            $scope.$watchCollection(function () {
                return controller.chosenFriends;
            }, calcSubmitEnabled);

            controller.clearPlayers = function () {
                controller.chosenFriends = [];
                calcSubmitEnabled();
            };

            controller.setSinglePlayer = function () {
                controller.playersEnabled = false;
                controller.desiredPlayerCount = SINGLE_PLAYER;
                controller.gamePace = 'Live';
                controller.wordPhraseSetter = SYSTEM_PUZZLES;
                controller.winners = 'SingleWinner';
                controller.h2hEnabled = false;
                controller.alternatingEnabled = false;
                controller.allFinishedEnabled = false;
                controller.turnBasedEnabled = false;
                controller.chosenFriends = [];
                calcSubmitEnabled();
            };

            controller.setTwoPlayers = function () {
                controller.playersEnabled = true;
                controller.desiredPlayerCount = TWO_PLAYERS;
                controller.h2hEnabled = true;
                controller.alternatingEnabled = true;
                controller.allFinishedEnabled = true;
                controller.turnBasedEnabled = true;
                if (controller.chosenFriends.length > 1) {
                    controller.chosenFriends = [];
                }
                calcSubmitEnabled();
            };

            controller.setThreePlayers = function () {
                controller.playersEnabled = true;
                controller.desiredPlayerCount = MULTI_PLAYER;
                controller.h2hEnabled = false;
                controller.alternatingEnabled = true;
                controller.allFinishedEnabled = true;
                controller.turnBasedEnabled = true;
                if (controller.wordPhraseSetter === 'Head2Head') {
                    controller.wordPhraseSetter = SYSTEM_PUZZLES;
                }
                calcSubmitEnabled();
            };

            controller.createGame = function () {
                twAds.showAdPopup().then(function () {
                    var featureNames = ['wordPhraseSetter', 'desiredPlayerCount', 'thieving', 'drawGallows', 'drawFace', 'gamePace', 'winners'];
                    var featureSet = [];
                    featureSet = featureSet.concat(featureNames.map(function (name) {
                        var data = controller[name];
                            if ((angular.isDefined(data)) && (data !== '')) {
                                return data;
                            }
                            return '';
                        }
                    ).filter(function (item) {
                        return item !== '';
                    }));

                    var players = controller.chosenFriends.map(function (player) {
                        return player.md5;
                    });
                    var playersAndFeatures = {'players': players, 'features': featureSet};
                    jtbBootstrapGameActions.new(playersAndFeatures);
                });
            };

            controller.showInvite = function () {
                $uibModal.open({
                    templateUrl: 'views/core-bs/friends/invite-friends.html',
                    controller: 'CoreBootstrapInviteCtrl',
                    controllerAs: 'invite',
                    size: 'lg',
                    resolve: {
                        invitableFriends: function () {
                            return controller.invitableFBFriends;
                        },
                        message: function () {
                            return 'Come play ' + jtbAppLongName + ' with me!';
                        }
                    }
                });
            };

            //  Initialize
            controller.setSinglePlayer();
        }
    ]
);
