'use strict';

angular.module('twistedHangmanApp')
    //  TODO - finish and move to core?
    .controller('CoreSignedInCtrl',
    ['$location', '$rootScope', '$cacheFactory',
        function ($location, $rootScope, $cacheFactory) {

            function clearHttpCache() {
                $cacheFactory.get('$http').removeAll();
            }

            function onSuccessfulLogin() {
                console.log('Logged in');
                clearHttpCache();
                $rootScope.$broadcast('login');
                $location.path('/');
            }

            onSuccessfulLogin();

        }
    ]
);