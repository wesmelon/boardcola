(function() {
  'use strict';

  angular
    .module('boardcola.board', ['resources.boards', 'resources.sticky', 'resources.stickies', 'toastr', 'resources.custom'])
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
      Stickies.query({bid: vm.bid}, setData);
    }

    function addSticky() {
      var newSticky = new Stickies({
        bid: vm.bid, 
        content: vm.content
      });
      vm.content = '';
      
      newSticky.$save(onSuccess, onFailure);
    };

    function updateSticky(sticky) {
      sticky.$save(onSuccess, onFailure);
    };

    function removeSticky(sticky) {
      sticky.$remove(onSuccess, onFailure);
    }

    function setData(stickies) {
      vm.stickies = stickies;
    }

    function onSuccess() {
      getStickies();
    }

    function onFailure(data) {
      toastr.error("There was an error: " + data);
    }
  };
})();