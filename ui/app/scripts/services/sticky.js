(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('sticky', sticky);

  sticky.$inject = ['$resource'];

  /**
   * The sticky factory.
   */
  function sticky($resource) {
    return $resource('api/stickies/:bid')
  };
})();