(function() {
  'use strict';

  angular
    .module('resources.sticky', [])
    .factory('stickyService', stickyService);

  stickyService.$inject = ['$resource'];

  /**
   * The sticky factory.
   */
  function stickyService($resource) {
    return $resource('api/stickies/:id', null, {
      'update': { method: 'PUT' }
    });
  };
})();