(function() {
  'use strict';

  angular
    .module('resources.categories', [])
    .factory('Categories', Categories);

  Categories.$inject = ['$resource'];

  /**
   * The category factory.
   */
  function Categories($resource) {
    return $resource('api/categories/:id');
  };
})();