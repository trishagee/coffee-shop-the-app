var demoApp = angular.module('demoApp', [])

demoApp.controller('SimpleController', function ($scope) {
    $scope.customers = [
        {name: 'John', city: 'Phoenix'},
        {name: 'John', city: 'New York'},
        {name: 'Jane', city: 'San Francisco'}
    ]
});

