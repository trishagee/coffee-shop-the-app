package com.mechanitis.demo.coffee;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @Path("nearest/{latitude}/{longitude}")
    @GET
    @Timed
    public CoffeeShop getNearest(@PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
        DBCollection collection = mongoDatabase.getCollection("coffeeshop");
        DBObject coffeeShop = collection.findOne(new BasicDBObject("location",
                                                                   new BasicDBObject("$near",
                                                                                     new BasicDBObject("$geometry",
                                                                                                       new BasicDBObject("type", "Point")
                                                                                                       .append("coordinates",
                                                                                                               asList(longitude,
                                                                                                                      latitude)))
                                                                                     .append("$maxDistance", 2000))));
        return new CoffeeShop((String) coffeeShop.get("name"), coffeeShop);
    }

    @Path("dummy")
    @GET
    @Timed
    public CoffeeShop getDummy() {
        return new CoffeeShop("A dummy coffee shop", new BasicDBObject("some", "thing"));
    }

}