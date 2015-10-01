'use strict';

/*global app: false */

/**
 * The logout controller.
 */
app.controller('LogoutCtrl', ['$auth', '$location', function($auth, $location) {
  if (!$auth.isAuthenticated()) {
    return;
  }
  $auth.logout()
    .then(function() {

      $location.path('/');
    });
}]);