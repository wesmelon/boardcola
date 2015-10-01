'use strict';

/*global app: false */

/**
 * The home controller.
 */
app.controller('HomeCtrl', ['$rootScope', '$scope', 'Account', function($rootScope, $scope, Account) {

  /**
   * Initializes the controller.
   */
  $scope.init = function() {
    Account.getProfile()
      .success(function(data) {
        $rootScope.user = data;
      })
      .error(function(error) {

      });
  };
}]);