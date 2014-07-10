package com.mechanitis.demo.coffee;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import java.net.UnknownHostException;

public class CoffeeShopService extends Service<CoffeeShopConfiguration> {

    public static void main(String[] args) throws Exception {
        new CoffeeShopService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CoffeeShopConfiguration> bootstrap) {
        bootstrap.setName("coffee-shop");
        AssetsBundle bundle = new AssetsBundle("/html/", "/");
        bootstrap.addBundle(bundle);
    }

    @Override
    public void run(CoffeeShopConfiguration configuration,
                    Environment environment) {
        MongoClient mongoClient;

        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not connect to MongoDB", e);
        }

        environment.manage(new MongoClientManager(mongoClient));
        environment.addResource(new CoffeeShopResource(mongoClient));
    }
}