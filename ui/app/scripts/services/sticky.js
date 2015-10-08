(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('stickyService', stickyService);

  stickyService.$inject = ['$resource'];

  /**
   * The sticky factory.
   */
  function stickyService($resource) {
    return $resource('api/stickies/:id')
  };
})();