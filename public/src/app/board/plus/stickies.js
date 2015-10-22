(function() {
  'use strict';

  angular
    .module('boardcola.board.plus.sticky', [])
    .directive('addStickyForm', addStickyForm);

  function addStickyForm() {
    return {
      restrict: 'A',
      templateUrl: '/src/app/board/plus/addSticky.tpl.html'
    }
  };
})();