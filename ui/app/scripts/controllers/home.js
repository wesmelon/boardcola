(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('HomeCtrl', HomeCtrl);

  HomeCtrl.$inject = ['$rootScope', '$scope', 'category', 'account'];

  /**
   * The home controller.
   */
  function HomeCtrl($rootScope, $scope, category, account) {
    var vm = $rootScope;
    vm.user = [];
    vm.categories = [];

    activate();

    function activate() {
      getProfile();
      getCategories();
    }

    function getProfile() {
      return account.getProfile()
        .then(function(data) {
          vm.user = data;
          return vm.user;
        });
    }

    function getCategories() {
      return category.getCategories()
        .then(function(data) {
          vm.categories = data;
          return vm.categories;
        });
    }

  };
})();