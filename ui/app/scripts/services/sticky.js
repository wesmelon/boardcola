(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('sticky', sticky);

  sticky.$inject = ['$http'];

  /**
   * The sticky factory.
   */
  function sticky($http) {
    return {
      getStickies: getStickies
    };

    function getStickies() {
      return $http.get('api/stickies/1')
        .then(getStickiesSuccess)
        .catch(getStickiesFailed);

      function getStickiesSuccess(response) {
        return response.data;
      }

      function getStickiesFailed(error) {
        console.log('XHR failed for getBoards.' + error.data);
      }
    }
  };
})();