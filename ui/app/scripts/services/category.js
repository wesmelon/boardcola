(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('category', category);

  category.$inject = ['$http'];

  /**
   * The category factory.
   */
  function category($http) {
    return {
      getCategories: getCategories
    };

    function getCategories() {
      return $http.get('api/categories')
        .then(getCategoriesSuccess)
        .catch(getCategoriesFailed);

      function getCategoriesSuccess(response) {
        return response.data;
      }

      function getCategoriesFailed(error) {
        console.log('XHR failed for getCategories.' + error.data);
      }
    }
  };
})();