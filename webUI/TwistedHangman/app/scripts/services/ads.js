/*global invokeApplixirVideoUnitExtended:false */
'use strict';

angular.module('twistedHangmanApp').factory('twAds',
    ['$uibModal', '$q',
        function ($uibModal, $q) {
            var TIME_BETWEEN_ADS = 2 * 60 * 1000;  // 2 minutes
            var lastAd = new Date(0);
            return {
                showAdPopup: function () {
                    var adPromise = $q.defer();
                    if (((new Date()) - lastAd ) >= TIME_BETWEEN_ADS) {
                        try {
                            invokeApplixirVideoUnitExtended(false, 'middle', function () {
                                adPromise.resolve();
                                lastAd = new Date();
                            });
                        } catch (ex) {
                            console.log(JSON.stringify(ex));
                            adPromise.resolve();
                        }
                    } else {
                        adPromise.resolve();
                    }
                    return adPromise.promise;
                }
            };
        }
    ]);
