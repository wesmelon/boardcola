(function() {
  'use strict';

  angular
    .module('resources.custom', [])
    .factory('Resource', Resource);

  Resource.$inject = ['$resource'];

  // Custom resource factory
  function Resource($resource) {
    return function(url, params, methods) {
      var defaults = {
        update: { method: 'PUT', isArray: false },
        create: { method: 'POST' }
      };

      methods = angular.extend(defaults, methods);
      var resource = $resource(url, params, methods);

      resource.prototype.$save = function(data, error) {
        if(!this.id) {
          return this.$create(data, error);
        } else {
          return this.$update(data, error);
        }
      };

      return resource;
    };
  };
})();