'use strict';

angular.module('twistedHangmanApp').factory('twAds',
  ['$modal', '$q',
    function ($modal, $q) {
      return {
        showAdPopup: function () {
          var p = $q.defer();

          p.resolve();
          return {result: p.promise};
          /*
          return $modal.open({
            templateUrl: 'views/popupAdDialog.html',
            controller: 'PopupAdCtrl',
            size: 'lg'
          });
           */
        }
      };
    }
  ]);
