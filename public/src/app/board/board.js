(function() {
  'use strict';

  angular
    .module('boardcola')
    .controller('BoardCtrl', BoardCtrl)
    .directive('boardcolaSticky', StickyDir);

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
  };

  function StickyDir() {
     return {
      restrict: 'AE',
      templateUrl: '/src/app/board/sticky.tpl.html',
      link: function($scope, element, attrs) {
        var inputElement = angular.element(element.children()[1]);

        element.addClass('edit-in-place');

        $scope.editing = false;

        $scope.editContent = function() {
          $scope.editing = true;
          element.addClass('active');
          inputElement[0].focus();
        };

        $scope.leaveInput = function() {
          $scope.editing = false;
          element.removeClass('active');
        }
      }
    }
  }
})();