var coffeeApp = angular.module('coffeeApp', ['ngResource'])

coffeeApp.factory('CoffeeShopLocator', function ($resource) {
    return $resource('http://localhost:8080/service/coffeeshop/nearest/:latitude/:longitude',
        {latitude: '@latitude', longitude: '@longitude'}, {}
    )
});

coffeeApp.controller('CoffeeShopController', function ($scope, $window, CoffeeShopLocator) {
    $scope.supportsGeo = $window.navigator;
    $scope.getNearestCoffeeShop = function (latitude, longitude) {
        $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: latitude, longitude: longitude});
        if ($scope.nearestCoffeeShop.name == null) {
            //default coffee shop
            $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: 51.4994678, longitude: -0.128888});
        }
    };
    $scope.getGeoLocation = function () {
        window.navigator.geolocation.getCurrentPosition(function (position) {
            $scope.getNearestCoffeeShop(position.coords.latitude, position.coords.longitude)
        }, function (error) {
            alert(error);
        });
    };
});

