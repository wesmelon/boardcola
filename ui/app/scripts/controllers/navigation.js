'use strict';

/*global app: false */

/**
 * The navigation controller.
 */
app.controller('NavigationCtrl', function($scope, $auth) {

  /**
   * Indicates if the user is authenticated or not.
   *
   * @returns {boolean} True if the user is authenticated, false otherwise.
   */
  $scope.isAuthenticated = function() {
    return $auth.isAuthenticated();
  };
});