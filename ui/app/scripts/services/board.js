(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('boardServices', boardServices);

  boardServices.$inject = ['$resource'];

  /**
   * The board factory.
   */
  function boardServices($resource) {
    return $resource('api/boards/:id')
  };
})();