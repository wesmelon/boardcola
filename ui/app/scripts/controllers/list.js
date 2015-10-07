(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('ListCtrl', ListCtrl);

  ListCtrl.$inject = ['$rootScope', '$scope', 'category', 'board'];

  /**
   * The home controller.
   */
  function ListCtrl($rootScope, $scope, category, board) {
    var vm = $rootScope;
    vm.categories = [];
    vm.boards = [];

    activate();

    function activate() {
      getCategories();
    }

    function getCategories() {
      return category.getCategories()
        .then(function(data) {
          vm.categories = data;
          return vm.categories;
        });
    }

    function getBoards() {
      return board.getBoards()
        .then(function(data) {
          vm.boards = data;
          return vm.boards;
        });
    }
  };
})();