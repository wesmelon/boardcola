(function() {
  'use strict';

  angular
    .module('boardcola.home', ['resources.account'])
    .controller('HomeCtrl', HomeCtrl);

  HomeCtrl.$inject = ['$rootScope', '$scope', 'account'];

  /**
   * The home controller.
   */
  function HomeCtrl($rootScope, $scope, account) {
    var vm = $rootScope;
    vm.user = [];

    activate();

    function activate() {
      getProfile();
    }

    function getProfile() {
      return account.getProfile()
        .then(function(data) {
          vm.user = data;
          return vm.user;
        });
    }
  };
})();