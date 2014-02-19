package com.mechanitis.demo.coffee;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static java.util.Arrays.asList;

@Path("/coffeeshop")
@Produces(MediaType.APPLICATION_JSON)
public class CoffeeShopResource {

    private final DB mongoDatabase;

    public CoffeeShopResource(DB mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @GET
    @Timed
    public CoffeeShop getNearest(@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude) {
        DBCollection collection = mongoDatabase.getCollection("coffeeshop");
        DBObject coffeeShop = collection.findOne(new BasicDBObject("location",
                                                                   new BasicDBObject("$near",
                                                                                     new BasicDBObject("$geometry",
                                                                                                       new BasicDBObject("type", "Point")
                                                                                                       .append("coordinates",
                                                                                                               asList(longitude,
                                                                                                                      latitude)))
                                                                                     .append("$maxDistance", 1000))));
        return new CoffeeShop((String) coffeeShop.get("name"), coffeeShop);
    }

}