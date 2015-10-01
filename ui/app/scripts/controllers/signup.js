'use strict';

/*global app: false */

/**
 * The sign up controller.
 */
app.controller('SignUpCtrl', ['$scope', '$auth', '$location', function($scope, $auth, $location) {

  /**
   * The submit method.
   */
  $scope.submit = function() {
    $auth.signup({
      username: $scope.username,
      email: $scope.email,
      password: $scope.password
    }).then(function(response) {

      $auth.setToken(response);
      $location.path('/');

    }).catch(function(response) {

    });
  };
}]);