(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('board', board);

  board.$inject = ['$resource'];

  /**
   * The board factory.
   */
  function board($resource) {
    return $resource('api/boards/:id')
  };
})();