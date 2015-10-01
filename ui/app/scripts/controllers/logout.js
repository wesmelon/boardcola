'use strict';

/*global app: false */

/**
 * The logout controller.
 */
app.controller('LogoutCtrl', function($auth, $location, toastr) {
  if (!$auth.isAuthenticated()) { return; }

  $auth.logout()
    .then(function() {
      toastr.info('You have been logged out');
      $location.path('/login');
    });
});