package com.mechanitis.demo.coffee

import com.mongodb.MongoClient
import spock.lang.Specification

class CoffeeShopResourceSpecification extends Specification {
    def 'should return a dummy shop for testing'() {
        given:
        def coffeeShop = new CoffeeShopResource(null)

        when:
        def nearestShop = coffeeShop.getDummy()

        then:
        nearestShop.name == 'A dummy coffee shop'
    }

    def 'should return Cafe Nero as the closest coffee shop to Westminster Abbey'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient.getDB("TrishaCoffee"))

        when:
        double latitude = 51.4994678
        double longitude = -0.128888
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        nearestShop.name == 'Caffè Nero'
        println nearestShop.allValues
    }

    def 'should return Costa as the closest coffee shop to Earls Court Road'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient.getDB("TrishaCoffee"))

        when:
        double latitude = 51.4950233
        double longitude = -0.1962431
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        nearestShop.name == 'Costa'
        println nearestShop.allValues
    }

    def 'should return null if no coffee shop found'() {
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient.getDB("TrishaCoffee"))

        when:
        double latitude = 37.3981841
        double longitude = -5.9776375999999996
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        nearestShop == null
    }


}
