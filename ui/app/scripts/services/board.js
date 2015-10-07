(function() {
  'use strict';

  angular
    .module('boardcola')
    .factory('board', board);

  board.$inject = ['$http'];

  /**
   * The board factory.
   */
  function board($http) {
    return {
      getBoards: getBoards
    };

    function getBoards() {
      return $http.get('api/boards/5')
        .then(getBoardsSuccess)
        .catch(getBoardsFailed);

      function getBoardsSuccess(response) {
        return response.data;
      }

      function getBoardsFailed(error) {
        console.log('XHR failed for getBoards.' + error.data);
      }
    }
  };
})();