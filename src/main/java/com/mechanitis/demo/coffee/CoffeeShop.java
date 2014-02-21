package com.mechanitis.demo.coffee;

import com.mongodb.DBObject;

import java.util.Map;

public class CoffeeShop {
    private final String name;

    private final DBObject allValues;

    public CoffeeShop(final String name, final DBObject allValues) {
        this.name = name;
        this.allValues = allValues;
    }

    public String getName() {
        return name;
    }

//    public Map<String, Object> getAllValues() {
//        return allValues.toMap();
//    }
}
