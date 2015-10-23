(function() {
  'use strict';

  angular
    .module('boardcola.board', ['resources.boards', 'resources.sticky', 'resources.stickies'])
    .controller('BoardCtrl', BoardCtrl);

  BoardCtrl.$inject = ['$stateParams', 'boardsServices', 'stickyService', 'stickiesService'];

  /**
   * The board controller.
   */
  function BoardCtrl($stateParams, boardsServices, stickyService, stickiesService) {
    var vm = this;
    var board_id = $stateParams.bid;

    vm.board = [];
    vm.stickies = [];
    vm.addSticky = addSticky;
    vm.saveSticky = saveSticky;
    vm.removeSticky = removeSticky;
    vm.content = '';

    activate();

    function activate() {
      getBoard();
      getStickies();
    }

    function getBoard() {
      boardsServices.get({id: board_id}, function(data) {
        vm.board = data;
      });
    }

    function getStickies() {
      stickiesService.query({id: board_id}, function(data) {
        vm.stickies = data;
      });
    }

    function addSticky() {
      var newSticky = new stickyService({
        bid: parseInt(board_id), 
        content: vm.content
      });
      vm.content = '';
      
      newSticky.$save(function() {
        getStickies();
      });
    };

    function saveSticky(sticky) {
      stickyService.update({ id: sticky.id }, sticky);
    };

    function removeSticky(sticky) {
      stickyService.remove({ id: sticky.id }, function() {
        getStickies();
      });
    }
  };
})();