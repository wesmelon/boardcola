(function() {
  'use strict';

  angular
    .module('resources.stickies', ['resources.custom'])
    .factory('Stickies', Stickies);

  Stickies.$inject = ['Resource'];

  /**
   * The sticky factory.
   */
  function Stickies($resource) {
    return $resource('api/boards/:bid/stickies/:id', 
      { bid: '@bid', id: '@id' }
    );
  };
})();