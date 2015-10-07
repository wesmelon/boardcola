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
      category.query(function(data) {
        vm.categories = data;
      });
    }

    function getBoards() {
      board.get({id: 1}, function(data) {
        vm.boards = data;
      });
    }

    function getStickies() {
      sticky.get({id: 1}, function(data) {
        vm.stickies = data;
      });
    }
  };
})();