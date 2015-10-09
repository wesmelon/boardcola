(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('ListCtrl', ListCtrl);

  ListCtrl.$inject = ['account', 'category', 'boardsServices'];

  /**
   * The list controller.
   */
  function ListCtrl(account, category, boardsServices) {
    var vm = this;
    vm.categories = [];
    vm.boards = [];
    vm.addBoard = addBoard;
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
      category.query(function(data) {
        vm.categories = data;
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
      newBoard.$save()
        .then(function(success) { 
          getBoards(); 
        })
        .catch(function(error) { 
          console.log(error); 
        });
    }
  };
})();