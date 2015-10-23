(function() {
  'use strict';

  angular
    .module('boardcola.board.sticky', [])
    .directive('boardcolaSticky', StickyDir);

    StickyDir.$inject = ['$parse']

    function StickyDir($parse) {
      return {
        restrict: 'AE',
        scope: {
          sticky: '=sticky',
          save: '&saveSticky',
          remove: '&removeSticky'
        },
        templateUrl: '/src/app/board/sticky.tpl.html',
        link: function(scope, element, attrs) {
          var fn = $parse(attrs.ngRightClick);
          element.bind('contextmenu', function(event) {
            scope.remove(scope.sticky);
            event.preventDefault();
          });

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