'use strict';

angular.module('twistedHangmanApp').factory('twAds',
  ['$modal',
    function ($modal) {
      return {
        showAdPopup: function () {
          return $modal.open({
            templateUrl: 'views/popupAdDialog.html',
            controller: 'PopupAdCtrl',
            size: 'lg'
          });
        }
      };
    }
  ]);
