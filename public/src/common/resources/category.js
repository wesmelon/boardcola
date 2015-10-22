(function() {
  'use strict';

  angular
    .module('resources.category', [])
    .factory('categoryServices', categoryServices);

  categoryServices.$inject = ['$resource'];

  /**
   * The category factory.
   */
  function categoryServices($resource) {
    return $resource('api/categories/:id');
  };
})();