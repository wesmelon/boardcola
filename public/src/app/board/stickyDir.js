(function() {
  'use strict';

  angular
    .module('boardcola.board.sticky', [])
    .directive('boardcolaSticky', StickyDir);

    function StickyDir() {
      return {
        restrict: 'AE',
        scope: {
          sticky: '=sticky',
          save: '&saveSticky'
        },
        templateUrl: '/src/app/board/sticky.tpl.html',
        link: function(scope, element, attrs) {

          var inputElement = angular.element(element.children()[1]);

          element.addClass('edit-in-place');

          scope.editing = false;

          scope.editContent = function() {
            scope.editing = true;
            element.addClass('active');
            inputElement[0].focus();
          };

          scope.leaveInput = function() {
            scope.save(scope.sticky);
            scope.editing = false;
            element.removeClass('active');
          }
        }
      }
    };

})();