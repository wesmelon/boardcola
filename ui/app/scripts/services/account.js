'use strict';

/*global app: false */

/**
 * The user factory.
 */
app.factory('Account', function($http) {
  return {
    getProfile: function() {
      return $http.get('/api/me');
    }
  };
});