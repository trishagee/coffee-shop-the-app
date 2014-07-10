package com.mechanitis.demo.coffee;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Arrays;

@Entity
public class CoffeeShop {
    private String name;
    private Location location;
    private int openStreetMapId;
    
    @Id
    private String id;

    public CoffeeShop(final String name) {
        this.name = name;
    }

    public CoffeeShop() {
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CoffeeShop{"
               + "name='" + name + '\''
               + ", location=" + location
               + ", openStreetMapId=" + openStreetMapId
               + ", id='" + id + '\''
               + '}';
    }

    private static class Location {
        private double[] coordinates = new double[2];
        private String type;
    }
}
