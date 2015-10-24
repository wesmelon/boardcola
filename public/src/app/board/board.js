(function() {
  'use strict';

  angular
    .module('boardcola.board', ['resources.boards', 'resources.sticky', 'resources.stickies', 'toastr'])
    .controller('BoardCtrl', BoardCtrl);

  BoardCtrl.$inject = ['$stateParams', 'Boards', 'Sticky', 'Stickies', 'toastr'];

  /**
   * The board controller.
   */
  function BoardCtrl($stateParams, Boards, Sticky, Stickies, toastr) {
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
      Boards.get({id: board_id}, function(data) {
        vm.board = data;
      });
    }

    function getStickies() {
      Stickies.query({id: board_id}, function(data) {
        vm.stickies = data;
      });
    }

    function addSticky() {
      var newSticky = new Sticky({
        bid: parseInt(board_id), 
        content: vm.content
      });
      vm.content = '';
      
      newSticky.$save(function() {
        getStickies();
      });
    };

    function saveSticky(sticky) {
      Sticky.update({ id: sticky.id }, sticky);
    };

    function removeSticky(sticky) {
      Sticky.remove({ id: sticky.id }, function() {
        getStickies();
        toastr.success('You have deleted sticky ' + sticky.id + '.');
      });
    }
  };
})();