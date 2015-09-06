/*global invokeApplixirVideoUnitExtended:false */
'use strict';

angular.module('twistedHangmanApp').factory('twAds',
    ['$modal', '$q',
      function ($modal, $q) {
      return {
        showAdPopup: function () {
          //  TODO - see how video ads work and revisit
          /*
          return $modal.open({
            templateUrl: 'views/popupAdDialog.html',
            controller: 'PopupAdCtrl',
            size: 'lg'
          });
           */
          var adPromise = $q.defer();
          invokeApplixirVideoUnitExtended(false, 'middle', function () {
            adPromise.resolve();
          });
          return {result: adPromise.promise};
        }
      };
    }
  ]);
