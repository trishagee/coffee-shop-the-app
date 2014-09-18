package com.mechanitis.demo.coffee;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.aggregation.GeoNear;

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
    private final Datastore datastore;

    public CoffeeShopResource(final MongoClient mongoClient) {
        datastore = new Morphia().createDatastore(mongoClient, "TrishaCoffee");
    }

    @Path("nearest/{latitude}/{longitude}")
    @GET
    public Object getNearest(@PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
        DBCollection collection = datastore.getDB().getCollection("coffeeshop");

        DBObject coffeeShop = collection.findOne(QueryBuilder.start("location").nearSphere(longitude, latitude).get());

        if (coffeeShop == null) {
            throw new WebApplicationException(404);
        }

        return coffeeShop;
    }

    @Path("{id}/order/")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveOrder(@PathParam("id") long coffeeShopId, Order order) {
        order.setCoffeeShopId(coffeeShopId);
        datastore.save(order);

        return Response.created(URI.create(order.getId().toString())).entity(order).build();
    }

    @Path("{id}/order/{orderId}")
    @GET
    public Order getOrder(@PathParam("id") long coffeeShopId, @PathParam("orderId") String orderId) {
        Order order = datastore.get(Order.class, new ObjectId(orderId));

        if (order == null) {
            throw new WebApplicationException(404);
        }
        return order;
    }

    @Path("dummy")
    @GET
    public CoffeeShop getDummy() {
        return new CoffeeShop("A dummy coffee shop");
    }

}