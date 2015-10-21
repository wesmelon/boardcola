(function() {
  'use strict';

  angular
    .module('boardcola')
    .directive('addStickyForm', addStickyForm);

  function addStickyForm() {
    return {
      restrict: 'A',
      templateUrl: '/src/app/board/plus/addSticky.tpl.html'
    }
  };
})();