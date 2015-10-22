(function() {
  'use strict';

  angular
    .module('boardcola.board.plus.sticky', [])
    .directive('stickyForm', stickyFormDir);

  function stickyFormDir() {
    return {
      restrict: 'A',
      scope: {
        add: '&addSticky',
        content: '=content'
      },
      templateUrl: '/src/app/board/plus/stickyForm.tpl.html'
    }
  };
})();