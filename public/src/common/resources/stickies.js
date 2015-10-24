(function() {
  'use strict';

  angular
    .module('resources.stickies', [])
    .factory('Stickies', Stickies);

  Stickies.$inject = ['$resource'];

  /**
   * The stickies factory.
   *  The difference from sticky is that this factory gets stickies by board id
   */
  function Stickies($resource) {
    return $resource('api/boards/:id/stickies');
  };
})();