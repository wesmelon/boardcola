(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('ListCtrl', ListCtrl);

  ListCtrl.$inject = ['account', 'boardsServices', 'categoryServices'];

  /**
   * The list controller.
   */
  function ListCtrl(account, boardsServices, categoryServices) {
    var vm = this;
    vm.categories = [];
    vm.boards = [];
    vm.addBoard = addBoard;
    vm.addCategory = addCategory;
    vm.name = '';

    activate();

    function activate() {
      getProfile().then(function(data) {
        vm.user = data;
      });
      getCategories();
      getBoards();
    }

    function getProfile() {
      return account.getProfile()
        .then(function(data) {
          return data;
        });
    }

    function getCategories() {
      categoryServices.query(function(data) {
        vm.categories = data;
      });
    }

    function addCategory() {
      var newCat = new categoryServices({
        name: vm.catName
      });

      vm.catName = '';
      newCat.$save(function() {
        getCategories()
      });
      
    }

    function getBoards() {
      boardsServices.query(function(data) {
        vm.boards = data;
      });
    }

    function addBoard() {
      var newBoard = new boardsServices({
        name: vm.name
      });

      vm.name = '';
      newBoard.$save(function() {
        getBoards()
      });
    }
  };
})();