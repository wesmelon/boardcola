(function() {
  'use strict';

  angular
    .module('boardcola.board', ['resources.boards', 'resources.sticky', 'resources.stickies', 'toastr'])
    .controller('BoardCtrl', BoardCtrl);

  BoardCtrl.$inject = ['$stateParams', 'Boards', 'Stickies', 'toastr'];

  /**
   * The board controller.
   */
  function BoardCtrl($stateParams, Boards, Stickies, toastr) {
    var vm = this;
    vm.bid = parseInt($stateParams.bid);

    vm.board = [];
    vm.stickies = [];
    vm.addSticky = addSticky;
    vm.updateSticky = updateSticky;
    vm.removeSticky = removeSticky;
    vm.content = '';

    activate();

    function activate() {
      getBoard();
      getStickies();
    }

    function getBoard() {
      Boards.get({id: vm.bid}, function(data) {
        vm.board = data;
      });
    }

    function getStickies() {
      Stickies.query({bid: vm.bid}, function(data) {
        vm.stickies = data;
      });
    }

    function addSticky() {
      var newSticky = new Stickies({
        bid: vm.bid, 
        content: vm.content
      });
      vm.content = '';
      
      newSticky.$save({bid: vm.bid}, onSuccess, onFailure);
    };

    function updateSticky(sticky) {
      Stickies.update({ bid: vm.bid, id: sticky.id }, sticky);
    };

    function removeSticky(sticky) {
      sticky.$remove({ bid: vm.bid, id: sticky.id }, onSuccess, onFailure);
    }

    function onSuccess() {
      getStickies();
    }

    function onFailure(data) {
      toastr.error("There was an error: " + data);
    }
  };
})();