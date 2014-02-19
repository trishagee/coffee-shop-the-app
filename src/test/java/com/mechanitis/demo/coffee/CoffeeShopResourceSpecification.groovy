package com.mechanitis.demo.coffee

import com.mongodb.MongoClient
import spock.lang.Specification

class CoffeeShopResourceSpecification extends Specification {
    def 'should get stuff from the database'() {
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

}
