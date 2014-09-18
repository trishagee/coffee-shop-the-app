package com.mechanitis.demo.coffee

import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import org.bson.types.ObjectId
import spock.lang.Ignore
import spock.lang.Specification

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

class CoffeeShopResourceSpecification extends Specification {
    def 'should return a dummy shop for testing'() {
        given:
        def mongoClient = Mock(MongoClient)
        def coffeeShop = new CoffeeShopResource(mongoClient)

        when:
        def nearestShop = coffeeShop.getDummy()

        then:
        nearestShop.name == 'A dummy coffee shop'
    }

    def 'should return Cafe Nero as the closest coffee shop to Westminster Abbey'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient)

        when:
        double latitude = 51.4994678
        double longitude = -0.128888
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        nearestShop.name == 'Caffè Nero'
    }

    def 'should return Costa as the closest coffee shop to Earls Court Road'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient)

        when:
        double latitude = 51.4950233
        double longitude = -0.1962431
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        nearestShop.name == 'Costa'
        println nearestShop
    }

    def 'should return closest coffee shop to Portland Conference Center'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient)

        when:
        double latitude = 45.5285859
        double longitude = -122.6631354
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        nearestShop.name == 'Tiny\'s Coffee'
        println nearestShop
    }

    @Ignore('No limit on distance so will always return something if the DB has been populated.')
    def 'should throw an exception if no coffee shop found'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient)

        when:
        double latitude = 0
        double longitude = 0
        def nearestShop = coffeeShop.getNearest(latitude, longitude)

        then:
        def exception = thrown(WebApplicationException)
        exception.response.status == Response.Status.NOT_FOUND.statusCode
    }

    def 'should give me back the order ID when an order is successfully created'() {
        given:
        def collection = Mock(DBCollection)
        collection.getName() >> 'CollectionName'
        def database = Mock(DB)
        database.getCollection(_) >> { collection }
        def mongoClient = Mock(MongoClient)
        mongoClient.getDB(_) >> { database }

        def coffeeShop = new CoffeeShopResource(mongoClient)
        def order = new Order(new String[0], new DrinkType('espresso', 'coffee'), 'medium', 'Me')

        //set ID for testing
        def orderId = new ObjectId()
        order.setId(orderId.toString())

        when:
        Response response = coffeeShop.saveOrder(75847854, order);

        then:
        response != null
        response.status == Response.Status.CREATED.statusCode
        response.headers['Location'][0].toString() == orderId.toString()
    }

    //functional test
    def 'should save all fields to the database when order is saved'() {
        given:
        def mongoClient = new MongoClient()
        def database = mongoClient.getDB("TrishaCoffee")
        def collection = database.getCollection('Order')
        collection.drop();

        def coffeeShop = new CoffeeShopResource(mongoClient)

        String[] orderOptions = ['soy milk']
        def drinkType = new DrinkType('espresso', 'coffee')
        def size = 'medium'
        def coffeeDrinker = 'Me'
        def order = new Order(orderOptions, drinkType, size, coffeeDrinker)
        def coffeeShopId = 89438

        when:
        Response response = coffeeShop.saveOrder(coffeeShopId, order);

        then:
        collection.count == 1
        def createdOrder = collection.findOne()
        println createdOrder
        createdOrder['selectedOptions'] == orderOptions
        createdOrder['type'].name == drinkType.name
        createdOrder['type'].family == drinkType.family
        createdOrder['size'] == size
        createdOrder['drinker'] == coffeeDrinker
        createdOrder['coffeeShopId'] == coffeeShopId
        createdOrder['_id'] != null
        createdOrder['prettyString'] == null

        cleanup:
        mongoClient.close()
    }

    //functional test
    def 'should return me an existing order'() {
        given:
        def mongoClient = new MongoClient()
        def coffeeShop = new CoffeeShopResource(mongoClient)
        def expectedOrder = new Order([] as String[], new DrinkType('filter', 'coffee'), 'super small', 'Yo')

        def coffeeShopId = 89438
        coffeeShop.saveOrder(coffeeShopId, expectedOrder);

        when:
        Order actualOrder = coffeeShop.getOrder(coffeeShopId, expectedOrder.getId());

        then:
        actualOrder != null
        actualOrder == expectedOrder

        cleanup:
        mongoClient.close()
    }

    //functional test
    def 'should throw a 404 if the order is not found'() {
        given:
        def mongoClient = new MongoClient()
        def database = mongoClient.getDB("TrishaCoffee")
        def coffeeShop = new CoffeeShopResource(mongoClient)

        when:
        coffeeShop.getOrder(7474, new ObjectId().toString());

        then:
        def e = thrown(WebApplicationException)
        e.response.status == 404

        cleanup:
        mongoClient.close()
    }

}
