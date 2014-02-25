package com.mechanitis.demo.coffee;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
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
        DBCollection underlyingCollection = mongoDatabase.getCollection("orders");
        JacksonDBCollection<Order, String> collection = JacksonDBCollection.wrap(underlyingCollection, Order.class, String.class);

        //this can be done client side or server side
        if (order.getCoffeeShopId() == 0) {
            order.setCoffeeShopId(coffeeShopId);
        }

        WriteResult<Order, String> writeResult = collection.insert(order);
        if (writeResult == null) {
            return Response.serverError().entity(writeResult.getError()).build();
        }
        setGeneratedIdOnOrder(writeResult, order);

        return Response.created(URI.create(order.getId().toString())).entity(order).build();
    }

    @Path("{id}/order/{orderId}")
    @GET
    public Order getOrder(@PathParam("id") long coffeeShopId, @PathParam("orderId") String orderId) {
        DBCollection underlyingCollection = mongoDatabase.getCollection("orders");
        JacksonDBCollection<Order, ObjectId> collection = JacksonDBCollection.wrap(underlyingCollection, Order.class, ObjectId.class);

        Order order = collection.findOne(DBQuery.is("_id", orderId));
        if (order != null) {
            return order;
        } else {
            throw new WebApplicationException(404);
        }
    }

    @Path("dummy")
    @GET
    public CoffeeShop getDummy() {
        return new CoffeeShop("A dummy coffee shop", new BasicDBObject("some", "thing"));
    }

    private void setGeneratedIdOnOrder(final WriteResult<Order, String> writeResult, final Order order) {
        String savedId = writeResult.getSavedId();
        if (savedId != null) {
            //if it's not null, it was generated.  If it's null, it already existed on the order and didn't need to be generated
            order.setId(savedId);
        }
    }

}