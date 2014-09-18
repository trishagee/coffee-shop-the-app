package com.mechanitis.demo.coffee;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Arrays;

@Entity
public class CoffeeShop {
    @Id
    private String id;
    
    private String name;
    private Location location;
    private int openStreetMapId;

    public CoffeeShop(final String name) {
        this.name = name;
    }

    public CoffeeShop() {
    }

    public CoffeeShop(final String name, final Location location, final int openStreetMapId, final String id) {
        this.name = name;
        this.location = location;
        this.openStreetMapId = openStreetMapId;
        this.id = id;
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

    static class Location {
        private double[] coordinates = new double[2];
        private String type;

        Location(final double[] coordinates, final String type) {
            this.coordinates = coordinates;
            this.type = type;
        }
    }
}
