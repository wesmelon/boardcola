(function() {
  'use strict';

  angular
    .module('boardcola')
    .directive('addStickyForm', addSticky);

  function addSticky() {
  	return {
  		restrict: 'A',
  		templateUrl: '/src/common/directives/addSticky.tpl.html'
  	}
  };
})();