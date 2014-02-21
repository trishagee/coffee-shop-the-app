var coffeeApp = angular.module('coffeeApp', ['ngResource'])

coffeeApp.controller('CustomerController', function ($scope) {
    $scope.customers = [
        {name: 'John', city: 'Phoenix'},
        {name: 'John', city: 'New York'},
        {name: 'Jane', city: 'San Francisco'}
    ]
});

coffeeApp.factory('CoffeeShopLocator', function ($resource) {
    return $resource('http://localhost:8080/service/coffeeshop/nearest/:latitude/:longitude'
        , {latitude: '@latitude', longitude: '@longitude'}
        , {}
    )
});

coffeeApp.controller('CoffeeShopController', function ($scope, $window, CoffeeShopLocator) {
//    $scope.defaultCoffeeShop = CoffeeShopLocator.get({latitude: 51.4994678, longitude: -0.128888});
//    $scope.nearestCoffeeShop = CoffeeShopLocator.get();
    $scope.getNearest = function (latitude, longitude) {
        $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: latitude, longitude: longitude});
        if ($scope.nearestCoffeeShop.name == null) {
            //default coffee shop
            $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: 51.4994678, longitude: -0.128888});
        }
    };
//    $scope.nearestCoffeeShop = 'My Shop';
//});
//
//coffeeApp.controller('GeoLocation', function ($scope, $window) {
    $scope.supportsGeo = $window.navigator;
    $scope.getGeoLocation = function () {
        window.navigator.geolocation.getCurrentPosition(function (position) {
            $scope.getNearest(position.coords.latitude, position.coords.longitude)
        }, function (error) {
            alert(error);
        });
    };
});

