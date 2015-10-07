(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('stickiesService', stickiesService);

  stickiesService.$inject = ['$resource'];

  /**
   * The stickies factory.
   *  The difference from sticky is that this factory gets stickies by board id
   */
  function stickiesService($resource) {
    return $resource('api/stickies/b/:bid')
  };
})();