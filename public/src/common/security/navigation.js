(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('NavigationCtrl', NavigationCtrl);

  NavigationCtrl.$inject = ['$scope', '$auth'];

  /**
   * The navigation controller.
   */
  function NavigationCtrl($scope, $auth) {

    /**
     * Indicates if the user is authenticated or not.
     *
     * @returns {boolean} True if the user is authenticated, false otherwise.
     */
    $scope.isAuthenticated = function() {
      return $auth.isAuthenticated();
    };
  };
})();