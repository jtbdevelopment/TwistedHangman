/*global invokeApplixirVideoUnitExtended:false */
'use strict';

angular.module('twistedHangmanApp').factory('twAds',
    ['$uibModal', '$q',
        function ($uibModal, $q) {
            return {
                showAdPopup: function () {
                    var adPromise = $q.defer();
                    try {
                        invokeApplixirVideoUnitExtended(false, 'middle', function () {
                            adPromise.resolve();
                        });
                    } catch (ex) {
                        console.log(JSON.stringify(ex));
                        adPromise.resolve();
                    }
                    return adPromise.promise;
                }
            };
        }
    ]);
