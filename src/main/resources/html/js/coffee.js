var coffeeApp = angular.module('coffeeApp', ['ngResource', 'ui.bootstrap'])

coffeeApp.factory('CoffeeShopLocator', function ($resource) {
    return $resource('/service/coffeeshop/nearest/:latitude/:longitude',
        {latitude: '@latitude', longitude: '@longitude'}, {}
    )
});

coffeeApp.factory('CoffeeOrder', function ($resource) {
    return $resource('/service/coffeeshop/:id/order/',
        {id: '@coffeeShopId'}, {}
    );
});

coffeeApp.service('LocalCoffeeShop', function () {
    var localCoffeeShop;
    return {
        setShop: function (shop) {
            localCoffeeShop = shop;
        },
        getShop: function () {
            return localCoffeeShop;
        }
    };
});

coffeeApp.controller('CoffeeShopController', function ($scope, $window, CoffeeShopLocator, LocalCoffeeShop) {
    $scope.supportsGeo = $window.navigator;
    $scope.getCoffeeShopAt = function (latitude, longitude) {
        $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: latitude, longitude: longitude});
        if ($scope.nearestCoffeeShop.name == null) {
            //default coffee shop
            $scope.nearestCoffeeShop = CoffeeShopLocator.get({latitude: 51.4994678, longitude: -0.128888});
        }
        LocalCoffeeShop.setShop($scope.nearestCoffeeShop);
    };
    $scope.findCoffeeShopNearestToMe = function () {
        window.navigator.geolocation.getCurrentPosition(function (position) {
            $scope.getCoffeeShopAt(position.coords.latitude, position.coords.longitude)
        }, function (error) {
            alert(error);
        });
    };
});

coffeeApp.controller('DrinksController', function ($scope, $filter, CoffeeOrder, LocalCoffeeShop) {
    //this could come from the coffee shop itself
    $scope.types = [
        {name: 'Americano', family: 'Coffee'},
        {name: 'Latte', family: 'Coffee'},
        {name: 'Cappuccino', family: 'Coffee'},
        {name: 'Tea', family: 'That Other Drink'}
    ]
    $scope.sizes = ['Small', 'Medium', 'Large']
    $scope.availableOptions = [
        {name: 'soy', appliesTo: 'milk'} ,
        {name: 'skimmed', appliesTo: 'milk'},
        {name: 'caramel', appliesTo: 'syrup'},
        {name: 'decaf', appliesTo: 'caffeine'},
        {name: 'whipped Cream', appliesTo: 'extras'},
        {name: 'vanilla', appliesTo: 'syrup'},
        {name: 'hazelnut', appliesTo: 'syrup'},
        {name: 'sugar free', appliesTo: 'syrup'},
        {name: 'non fat', appliesTo: 'milk'},
        {name: 'half fat', appliesTo: 'milk'},
        {name: 'half and half', appliesTo: 'milk'},
        {name: 'half caf', appliesTo: 'caffeine'},
        {name: 'chocolate powder', appliesTo: 'extras'},
        {name: 'double shot', appliesTo: 'preparation'},
        {name: 'wet', appliesTo: 'preparation'},
        {name: 'dry', appliesTo: 'preparation'},
        {name: 'organic', appliesTo: 'milk'},
        {name: 'extra hot', appliesTo: 'preparation'}
    ]
    $scope.messages = [];

    $scope.addOption = function () {
        if (!$scope.drink.selectedOptions) {
            $scope.drink.selectedOptions = [];
        }
        $scope.drink.selectedOptions.push($filter('lowercase')($scope.newOption));
        $scope.newOption = '';
    };
    $scope.giveMeCoffee = function () {
        var selectedShop = LocalCoffeeShop.getShop();
        if (selectedShop == null) {
            $scope.messages.push({type: 'danger', msg: 'You need to find your local coffee shop before you can submit an order'});
        }else {
            $scope.coffeeShopId = selectedShop.allValues.openStreetMapId;
            CoffeeOrder.save({ id: $scope.coffeeShopId}, $scope.drink,
                function (result) {
                    $scope.messages.push({type: 'success', msg: 'Order sent!', orderId: result.id});
                },
                function (errorResponse) {
                    $scope.messages.push({type: 'danger', msg: 'Something went wrong with your order.', error: errorResponse.data});
                });
        }
    };
    $scope.closeAlert = function (index) {
        $scope.messages.splice(index, 1);
    };

});