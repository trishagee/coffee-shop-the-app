package com.mechanitis.demo.coffee;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

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
        if (coffeeShop == null) {
            return null;
        }
        return new CoffeeShop((String) coffeeShop.get("name"), coffeeShop);
    }

    @Path("{id}/order/")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveOrder(@PathParam("id") long coffeeShopId, Order order) {
        JacksonDBCollection<Order, ObjectId> collection;
        DBCollection underlyingCollection = mongoDatabase.getCollection("orders");
        collection = JacksonDBCollection.wrap(underlyingCollection, Order.class, ObjectId.class);

        WriteResult<Order, ObjectId> writeResult = collection.insert(order);
        if (writeResult == null) {
            return Response.serverError().build();
        }
        order.setId(writeResult.getSavedId());

        return Response.created(URI.create(order.getId().toString())).build();
    }

    @Path("dummy")
    @GET
    public CoffeeShop getDummy() {
        return new CoffeeShop("A dummy coffee shop", new BasicDBObject("some", "thing"));
    }

}