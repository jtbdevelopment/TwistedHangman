'use strict';

angular.module('twistedHangmanApp').factory('jtbGameClassifier',
    ['jtbPlayerService', 'twGameDetails',
        function (jtbPlayerService, twGameDetails) {
            var YOUR_TURN = 'Your move.';
            var THEIR_TURN = 'Their move.';
            var OLDER = 'Older games.';

            var icons = {};
            icons[YOUR_TURN] = 'play';
            icons[THEIR_TURN] = 'pause';
            icons[OLDER] = 'stop';

            return {
                getClassifications: function () {
                    return [YOUR_TURN, THEIR_TURN, OLDER];
                },

                getClassification: function (game) {
                    var md5 = jtbPlayerService.currentPlayer().md5;
                    if (twGameDetails.playerActionRequired(game, md5) || game.gamePhase === 'RoundOver') {
                        return YOUR_TURN;
                    }

                    if (game.gamePhase === 'Declined' || game.gamePhase === 'Quit' || game.gamePhase === 'NextRoundStarted') {
                        return OLDER;
                    }

                    return THEIR_TURN;
                },

                getIcons: function () {
                    return icons;
                }
            };
        }
    ]
);