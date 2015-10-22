(function() {
  'use strict';

  angular
    .module('security.login', ['toastr'])
    .controller('LoginCtrl', LoginCtrl);

  LoginCtrl.$inject = ['$scope', '$auth', '$location', 'toastr'];

  /**
   * The sign in controller.
   */
  function LoginCtrl($scope, $auth, $location, toastr) {

    /**
     * Submits the login form.
     */
    $scope.submit = function() {
      $auth.setStorageType($scope.rememberMe ? 'localStorage' : 'sessionStorage');
      $auth.login({ email: $scope.email, password: $scope.password, rememberMe: $scope.rememberMe })
        .then(function() {
          toastr.success('You have signed in');
          $location.path('/');
        })
        .catch(function(response) {
          toastr.error(response.data.message, response.status);

        });
    };

    /**
     * Authenticate with a social provider.
     *
     * @param provider The name of the provider to authenticate.
     */
    $scope.authenticate = function(provider) {
      $auth.authenticate(provider)
        .then(function() {
          toastr.success('You have successfully signed in with ' + provider);
          $location.path('/');
        })
        .catch(function(response) {
          toastr.error(response.data.message);
        });
    };
  };
})();