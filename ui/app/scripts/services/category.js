(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('category', category);

  category.$inject = ['$resource'];

  /**
   * The category factory.
   */
  function category($resource) {
    return $resource('api/categories');
  };
})();