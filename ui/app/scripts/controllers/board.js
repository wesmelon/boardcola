(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('BoardCtrl', BoardCtrl);

  BoardCtrl.$inject = ['$rootScope', '$stateParams', 'boardServices', 'stickiesService'];

  /**
   * The home controller.
   */
  function BoardCtrl($rootScope, $stateParams, boardServices, stickiesService) {
    var vm = $rootScope;
    vm.board = [];
    vm.stickies = [];

    activate();

    function activate() {
      getBoard();
      getStickies();
    }

    function getBoard() {
      boardServices.get({id: $stateParams.bid}, function(data) {
        vm.board = data;
      });
    }

    function getStickies() {
      stickiesService.query({bid: $stateParams.bid}, function(data) {
        vm.stickies = data;
      });
    }
  };
})();