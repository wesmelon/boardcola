(function() {
  'use strict';

  angular
    .module('security.logout', ['toastr'])
    .controller('LogoutCtrl', LogoutCtrl);

  LogoutCtrl.$inject = ['$auth', '$location', 'toastr'];

  /**
   * The logout controller.
   */
  function LogoutCtrl($auth, $location, toastr) {
    if (!$auth.isAuthenticated()) { return; }

    $auth.logout()
      .then(function() {
        toastr.info('You have been logged out');
        $location.path('/login');
      });
  };
})();