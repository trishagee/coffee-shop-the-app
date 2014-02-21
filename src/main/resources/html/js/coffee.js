var coffeeApp = angular.module('coffeeApp', ['ngResource'])

coffeeApp.controller('CustomerController', function ($scope) {
    $scope.customers = [
        {name: 'John', city: 'Phoenix'},
        {name: 'John', city: 'New York'},
        {name: 'Jane', city: 'San Francisco'}
    ]
});

coffeeApp.factory('CoffeeShopLocator', function ($resource) {
//    return $resource('http://localhost:8080/service/coffeeshop/nearest/51.4994678/-0.128888/', {}, {
////        show: { method: 'GET' }
//    });

//    return $resource('http://localhost:8080/service/coffeeshop/dummy/', {}, {
//                get: { method: 'JSONP', params: { callback: 'JSON_CALLBACK'}}
//    });

    return $resource('http://localhost:8080/service/coffeeshop/nearest/:latitude/:longitude'
        ,{latitude:'@latitude', longitude:'@longitude'}
//        ,{}
        ,{}
    )
});

coffeeApp.controller('CoffeeShopController', function ($scope, CoffeeShopLocator) {
//    $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: 51.4994678, longitude: -0.128888});
//    $scope.nearestCoffeeShop = CoffeeShopLocator.get();
    $scope.getNearest = function() {
//        $scope.nearestCoffeeShop = CoffeeShopLocator.get();
    $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: 51.4994678, longitude: -0.128888});
    };
//    $scope.nearestCoffeeShop = 'My Shop';
});


//    http://localhost:8080/coffeeshop/nearest/51.4994678/-0.128888
//    http://localhost:8080/coffeeshop/nearest/51.4994678/-0.128888


