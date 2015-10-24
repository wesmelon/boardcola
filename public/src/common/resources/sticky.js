(function() {
  'use strict';

  angular
    .module('resources.sticky', [])
    .factory('Sticky', Sticky);

  Sticky.$inject = ['$resource'];

  /**
   * The sticky factory.
   */
  function Sticky($resource) {
    return $resource('api/stickies/:id', null, {
      'update': { method: 'PUT' },
      'remove': { method: 'DELETE' }
    });
  };
})();