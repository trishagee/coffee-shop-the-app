package com.mechanitis.demo.coffee;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DrinkType {
    private final String name;
    private final String family;

    public DrinkType(@JsonProperty("name") final String name,
                     @JsonProperty("family") final String family) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }
}
