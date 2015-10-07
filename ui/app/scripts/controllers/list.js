(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('ListCtrl', ListCtrl);

  ListCtrl.$inject = ['$rootScope', '$scope', 'category', 'board', 'sticky'];

  /**
   * The home controller.
   */
  function ListCtrl($rootScope, $scope, category, board, sticky) {
    var vm = $rootScope;
    vm.categories = [];
    vm.boards = [];
    vm.stickies = [];

    activate();

    function activate() {
      getCategories();
      getBoards();
      getStickies();
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

    function getStickies() {
      return sticky.getStickies()
        .then(function(data) {
          vm.stickies = data;
          return vm.stickies;
        });
    }
  };
})();