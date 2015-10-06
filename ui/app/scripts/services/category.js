'use strict';

/**
 * The category factory.
 */
app.factory('Category', ['$resource',
  function($resource){
    return $resource('api/categories', {}, {
      query: {method:'GET', isArray:true}
    });
  }]);