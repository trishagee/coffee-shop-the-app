var demoApp = angular.module('backend', [])

demoApp.factory('simpleFactory', function($http) {
    var customers = [
        {name: 'John', city: 'Phoenix'},
        {name: 'John', city: 'New York'},
        {name: 'Jane', city: 'San Francisco'}
    ];

    var factory = {};
    factory.getCustomers = function () {
        //this is where you make the ajax call
        return customers;
    };
    return factory;

})

demoApp.controller('SimpleController', function ($scope, simpleFactory) {
    $scope.customers = simpleFactory.getCustomers();
});

