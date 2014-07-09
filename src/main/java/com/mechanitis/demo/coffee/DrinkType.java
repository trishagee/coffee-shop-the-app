package com.mechanitis.demo.coffee;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DrinkType {
    private String name;
    private String family;

    public DrinkType(@JsonProperty("name") final String name,
                     @JsonProperty("family") final String family) {
        this.name = name;
        this.family = family;
    }

    public DrinkType() {
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    @Override public String toString() {
        return "DrinkType{"
               + "name='" + name + '\''
               + ", family='" + family + '\''
               + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DrinkType drinkType = (DrinkType) o;

        if (!family.equals(drinkType.family)) {
            return false;
        }
        if (!name.equals(drinkType.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + family.hashCode();
        return result;
    }
}
