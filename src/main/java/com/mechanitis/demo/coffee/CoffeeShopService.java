package com.mechanitis.demo.coffee;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import java.net.UnknownHostException;

public class CoffeeShopService extends Service<CoffeeShopConfiguration> {
    private DB database;
    private MongoClient mongoClient;

    public static void main(String[] args) throws Exception {
        new CoffeeShopService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CoffeeShopConfiguration> bootstrap) {
        bootstrap.setName("coffee-shop");
        AssetsBundle bundle = new AssetsBundle("/html/", "/");
        bootstrap.addBundle(bundle);
        try {
            mongoClient = new MongoClient();
            database = mongoClient.getDB("TrishaCoffee");
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not connect to MongoDB", e);
        }
    }

    @Override
    public void run(CoffeeShopConfiguration configuration,
                    Environment environment) {
        environment.addResource(new CoffeeShopResource(database));
    }
}