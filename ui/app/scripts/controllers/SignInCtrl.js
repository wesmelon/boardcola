'use strict';

/*global app: false */

/**
 * The sign in controller.
 */
app.controller('SignInCtrl', ['$scope', '$alert', '$auth', '$location', function($scope, $alert, $auth, $location) {

  /**
   * Submits the login form.
   */
  $scope.submit = function() {
    $auth.setStorageType($scope.rememberMe ? 'localStorage' : 'sessionStorage');
    $auth.login({ email: $scope.email, password: $scope.password, rememberMe: $scope.rememberMe })
      .then(function() {
        $alert({
          content: 'You have successfully signed in',
          animation: 'fadeZoomFadeDown',
          type: 'material',
          duration: 3
        });

        $location.path('/');
      })
      .catch(function(response) {
        console.log(response);
        $alert({
          content: response.data.message,
          animation: 'fadeZoomFadeDown',
          type: 'material',
          duration: 3
        });
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
        $alert({
          content: 'You have successfully signed in',
          animation: 'fadeZoomFadeDown',
          type: 'material',
          duration: 3
        });

        $location.path('/');
      })
      .catch(function(response) {
        $alert({
          content: response.data.message,
          animation: 'fadeZoomFadeDown',
          type: 'material',
          duration: 3
        });
      });
  };
}]);