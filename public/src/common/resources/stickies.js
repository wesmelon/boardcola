(function() {
  'use strict';

  angular
    .module('resources.stickies', [])
    .factory('Stickies', Stickies);

  Stickies.$inject = ['$resource'];

  /**
   * The sticky factory.
   */
  function Stickies($resource) {
    return $resource('api/boards/:bid/stickies/:id', { bid: '@bid', id: '@id' } , {
      'update': { method: 'PUT' }
    });
  };
})();