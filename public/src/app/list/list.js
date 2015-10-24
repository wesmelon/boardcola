(function() {
  'use strict';

  angular
    .module('boardcola.list', ['resources.boards', 'resources.categories'])
    .controller('ListCtrl', ListCtrl);

  ListCtrl.$inject = ['account', 'Boards', 'Categories'];

  /**
   * The list controller.
   */
  function ListCtrl(account, Boards, Categories) {
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
      Categories.query(function(data) {
        vm.categories = data;
      });
    }

    function addCategory() {
      var newCat = new Categories({
        name: vm.catName
      });

      vm.catName = '';
      newCat.$save(function() {
        getCategories()
      });
      
    }

    function getBoards() {
      Boards.query(function(data) {
        vm.boards = data;
      });
    }

    function addBoard() {
      var newBoard = new Boards({
        name: vm.name
      });

      vm.name = '';
      newBoard.$save(function() {
        getBoards()
      });
    }
  };
})();