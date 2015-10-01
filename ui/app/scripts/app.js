'use strict';

/**
 * @ngdoc overview
 * @name uiApp
 * @description
 * # boardcola
 *
 * Main module of the application.
 */
var app = angular
  .module('boardcola', ['ngResource', 'ngMessages', 'ngAnimate', 'ngCookies', 'ui.router', 'satellizer', 'toastr']);

app.config(function ($urlRouterProvider, $stateProvider, $httpProvider, $authProvider) {
  

  $stateProvider
    .state('home', { 
      url: '/', 
      templateUrl: '/views/home.html', 
      resolve: {
        loginRequired: loginRequired 
      }
    })
    .state('signup', { 
      url: '/signup', 
      templateUrl: '/views/signup.html',
      resolve: {
        skipIfLoggedIn: skipIfLoggedIn
      }
    })
    .state('login', { 
      url: '/login', 
      templateUrl: '/views/login.html',
      resolve: {
        skipIfLoggedIn: skipIfLoggedIn
      } 
    })
    .state('logout', { 
      url: '/logout', 
      template: null, 
      controller: 'LogoutCtrl' 
    });

  $urlRouterProvider.otherwise('/home');

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
          $injector.get('$state').go('login');
        }
        return $q.reject(rejection);
      }
    };
  });

  function skipIfLoggedIn($q, $auth) {
    var deferred = $q.defer();

    if ($auth.isAuthenticated()) {
      deferred.reject();
    } else {
      deferred.resolve();
    }
    return deferred.promise;
  }

  function loginRequired($q, $location, $auth) {
    var deferred = $q.defer();

    if (!$auth.isAuthenticated()) {
      $location.path('/login');
    } else {
      deferred.resolve();
    }

    return deferred.promise;
  }
});
