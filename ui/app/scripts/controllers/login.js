'use strict';

/*global app: false */

/**
 * The sign in controller.
 */
app.controller('LoginCtrl', ['$scope', '$auth', '$location', function($scope, $auth, $location) {

  /**
   * Submits the login form.
   */
  $scope.submit = function() {
    $auth.setStorageType($scope.rememberMe ? 'localStorage' : 'sessionStorage');
    $auth.login({ email: $scope.email, password: $scope.password, rememberMe: $scope.rememberMe })
      .then(function() {

        $location.path('/');
      })
      .catch(function(response) {
        console.log(response);

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

        $location.path('/');
      })
      .catch(function(response) {

      });
  };
}]);