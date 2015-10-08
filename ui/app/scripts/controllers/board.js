(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('BoardCtrl', BoardCtrl);

  BoardCtrl.$inject = ['$stateParams', 'boardServices', 'stickyService', 'stickiesService'];

  /**
   * The home controller.
   */
  function BoardCtrl($stateParams, boardServices, stickyService, stickiesService) {
    var vm = this;
    var board_id = $stateParams.bid;

    vm.board = [];
    vm.stickies = [];
    vm.addSticky = addSticky;
    vm.content = '';

    activate();

    function activate() {
      getBoard();
      getStickies();
    }

    function getBoard() {
      boardServices.get({id: board_id}, function(data) {
        vm.board = data;
      });
    }

    function getStickies() {
      stickiesService.query({bid: board_id}, function(data) {
        vm.stickies = data;
      });
    }

    function addSticky() {
      var newSticky = new stickyService({
        bid: parseInt(board_id), 
        content: vm.content
      });
      vm.content = '';
      newSticky.$save();
      console.log(newSticky);

      // Refresh board
      getStickies();
      
    };
  };
})();