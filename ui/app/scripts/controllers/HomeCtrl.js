'use strict';

/*global app: false */

/**
 * The home controller.
 */
app.controller('HomeCtrl', ['$rootScope', '$scope', '$alert', 'Account', function($rootScope, $scope, $alert, Account) {

  /**
   * Initializes the controller.
   */
  $scope.init = function() {
    Account.getProfile()
      .success(function(data) {
        $rootScope.user = data;
      })
      .error(function(error) {
        $alert({
          content: error.message,
          animation: 'fadeZoomFadeDown',
          type: 'material',
          duration: 3
        });
      });
  };
}]);