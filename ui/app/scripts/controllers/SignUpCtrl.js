'use strict';

/*global app: false */

/**
 * The sign up controller.
 */
app.controller('SignUpCtrl', ['$scope', '$alert', '$auth', function($scope, $alert, $auth) {

  /**
   * The submit method.
   */
  $scope.submit = function() {
    $auth.signup({
      username: $scope.username,
      email: $scope.email,
      password: $scope.password
    }).then(function() {
      $alert({
        content: 'You have successfully signed up',
        animation: 'fadeZoomFadeDown',
        type: 'material',
        duration: 3
      });
    }).catch(function(response) {
      $alert({
        content: response.data.message,
        animation: 'fadeZoomFadeDown',
        type: 'material',
        duration: 3
      });
    });
  };
}]);