(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('ListCtrl', ListCtrl);

  ListCtrl.$inject = ['$rootScope', 'account', 'category', 'boardsServices'];

  /**
   * The home controller.
   */
  function ListCtrl($rootScope, account, category, boardsServices) {
    var vm = $rootScope;
    vm.categories = [];
    vm.boards = [];

    activate();

    function activate() {
      getUserId().then(function(userId) {
        getCategories();
        getBoards(userId);
      });
    }

    function getUserId() {
      return account.getProfile()
        .then(function(data) {
          vm.user = data;
          return data.id;
        });
    }

    function getCategories() {
      category.query(function(data) {
        vm.categories = data;
      });
    }

    function getBoards(userId) {
      boardsServices.query({uid: userId}, function(data) {
        vm.boards = data;
      });
    }
  };
})();