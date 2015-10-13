(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('categoryServices', categoryServices);

  categoryServices.$inject = ['$resource'];

  /**
   * The category factory.
   */
  function categoryServices($resource) {
    return $resource('api/categories/:id');
  };
})();