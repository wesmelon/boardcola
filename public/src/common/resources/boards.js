(function() {
  'use strict';

  angular
    .module('resources.boards', [])
    .factory('boardsServices', boardsServices);

  boardsServices.$inject = ['$resource'];

  /**
   * The board factory.
   */
  function boardsServices($resource) {
    return $resource('api/boards/:id');
  };
})();