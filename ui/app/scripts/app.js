'use strict';

/**
 * @ngdoc overview
 * @name uiApp
 * @description
 * # uiApp
 *
 * Main module of the application.
 */
var app = angular
  .module('uiApp', [
    'ngResource',
    'ngMessages',
    'ngAnimate',
    'ngCookies',
    'ui.router',
    'satellizer',
    'mgcrea.ngStrap'
  ]);

app.run(function($rootScope) {
  /**
   * The user data.
   *
   * @type {{}}
   */
  $rootScope.user = {};
});

app.config(function ($urlRouterProvider, $stateProvider, $httpProvider, $authProvider) {
  
  $urlRouterProvider.otherwise('/home');

  $stateProvider
    .state('home', { 
      url: '/home', 
      templateUrl: '/views/home.html', resolve: {
        authenticated: function($q, $location, $auth) {
          var deferred = $q.defer();

          if (!$auth.isAuthenticated()) {
            $location.path('/signIn');
          } else {
            deferred.resolve();
          }

          return deferred.promise;
        }
      }
    })
    .state('signUp', { 
      url: '/signUp', 
      templateUrl: '/views/signUp.html' 
    })
    .state('signIn', { 
      url: '/signIn', 
      templateUrl: '/views/signIn.html' 
    })
    .state('signOut', { 
      url: '/signOut', 
      template: null, 
      controller: 'SignOutCtrl' 
    });

  $httpProvider.interceptors.push(function($q, $injector) {
    return {
      request: function(request) {
        // Add auth token for Silhouette if user is authenticated
        var $auth = $injector.get('$auth');
        if ($auth.isAuthenticated()) {
          request.headers['X-Auth-Token'] = $auth.getToken();
        }

        // Add CSRF token for the Play CSRF filter
        var cookies = $injector.get('$cookies');
        var token = cookies.get('PLAY_CSRF_TOKEN');
        if (token) {
          // Play looks for a token with the name Csrf-Token
          // https://www.playframework.com/documentation/2.4.x/ScalaCsrf
          request.headers['Csrf-Token'] = token;
        }

        return request;
      },

      responseError: function(rejection) {
        if (rejection.status === 401) {
          var $auth = $injector.get('$auth');
          $auth.logout();
          $injector.get('$state').go('signIn');
        }
        return $q.reject(rejection);
      }
    };
  });
});
