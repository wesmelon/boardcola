'use strict';

/*global app: false */

/**
 * The sign out controller.
 */
app.controller('SignOutCtrl', ['$auth', '$alert', '$location', function($auth, $alert, $location) {
  if (!$auth.isAuthenticated()) {
    return;
  }
  $auth.logout()
    .then(function() {
      $alert({
        content: 'You have been logged out',
        animation: 'fadeZoomFadeDown',
        type: 'material',
        duration: 3
      });

      $location.path('/');
    });
}]);