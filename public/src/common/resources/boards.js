(function() {
  'use strict';

  angular
    .module('resources.boards', [])
    .factory('Boards', Boards);

  Boards.$inject = ['$resource'];

  /**
   * The board factory.
   */
  function Boards($resource) {
    return $resource('api/boards/:id');
  };
})();