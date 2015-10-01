'use strict';

/*global app: false */

/**
 * The sign up controller.
 */
app.controller('SignUpCtrl', function($scope, $auth, $location, toastr) {

  $scope.submit = function() {
    $auth.signup({
      username: $scope.username,
      email: $scope.email,
      password: $scope.password
    }).then(function(response) {

      $auth.setToken(response);
      toastr.info('You have successfully created a new account and have been signed-in');
      $location.path('/');

    }).catch(function(response) {
      toastr.error(response.data.message);
      
    });
  };
});