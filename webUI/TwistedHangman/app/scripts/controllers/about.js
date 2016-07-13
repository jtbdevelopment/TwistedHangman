'use strict';

/**
 * @ngdoc function
 * @name twistedHangmanApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the twistedHangmanApp
 */
angular.module('twistedHangmanApp')
    .controller('AboutCtrl',
        ['$scope',
            function ($scope) {
                $scope.slides = [
                    {
                        title: 'Welcome to Twisted Hangman!',
                        image: '/images/info/basicgame.png',
                        text: 'A basic hangman game with some twists played in rounds.  Click the letter to guess it.',
                        id: 0
                    },
                    {
                        title: 'Such as stealing letters!',
                        image: '/images/info/thieving.png',
                        text: 'Turn on thieving and you can steal blank letters (for a price).',
                        id: 1
                    },
                    {
                        title: 'Solo Play Options..',
                        image: '/images/info/singleplayeroptions.png',
                        text: '..where you can set difficulty.',
                        id: 2
                    },
                    {
                        title: 'Play Friends ..',
                        image: '/images/info/multiplayeroptions.png',
                        text: '..with different pacing, challenges and winning options.',
                        id: 3
                    }
                ];

            }]);
