(function() {
  'use strict';

  angular
    .module('resources.account', [])
    .factory('account', account);

  account.$inject = ['$http'];

  /**
   * The user factory.
   */
  function account($http) {
    return {
      getProfile: getProfile
    };

    function getProfile() {
      return $http.get('api/me')
        .then(getMeSuccess)
        .catch(getMeFailed);

      function getMeSuccess(response) {
        return response.data;
      }

      function getMeFailed(error) {
        console.log('XHR failed for getProfile.' + error.data);
      }
    }
  };
})();