package com.mechanitis.demo.coffee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongodb.morphia.annotations.Id;

import java.util.Arrays;

public class Order {
    //    form = {
    //        "selectedOptions": [],
    //        "type": {
    //            "name": "Cappuccino",
    //            "family": "Coffee"
    //        },
    //        "size": "Small",
    //        "drinker": "Trisha"
    //    }
    private String[] selectedOptions;
    private DrinkType type;
    private String size;
    private String drinker;

    @Id
    private String id;

    @JsonProperty("shopId")
    private long coffeeShopId;

    public Order(@JsonProperty("selectedOptions") final String[] selectedOptions,
                 @JsonProperty("type") final DrinkType type,
                 @JsonProperty("size") final String size,
                 @JsonProperty("drinker") final String drinker) {
        this.selectedOptions = selectedOptions;
        this.type = type;
        this.size = size;
        this.drinker = drinker;
    }

    public Order() {
    }

    public String[] getSelectedOptions() {
        return selectedOptions;
    }

    public DrinkType getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getDrinker() {
        return drinker;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getCoffeeShopId() {
        return coffeeShopId;
    }

    public void setCoffeeShopId(final long coffeeShopId) {
        this.coffeeShopId = coffeeShopId;
    }

    @JsonIgnore
    public String getPrettyString() {
        return toString();
    }

    @Override
    public String toString() {
        return "Order{"
               + "selectedOptions=" + Arrays.toString(selectedOptions)
               + ", type=" + type
               + ", size='" + size + '\''
               + ", drinker='" + drinker + '\''
               + ", id=" + id
               + ", coffeeShopId=" + coffeeShopId
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

        Order order = (Order) o;

        if (coffeeShopId != order.coffeeShopId) {
            return false;
        }
        if (!drinker.equals(order.drinker)) {
            return false;
        }
        if (id != null ? !id.equals(order.id) : order.id != null) {
            return false;
        }
        if (!Arrays.equals(selectedOptions, order.selectedOptions)) {
            return false;
        }
        if (!size.equals(order.size)) {
            return false;
        }
        if (!type.equals(order.type)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(selectedOptions);
        result = 31 * result + type.hashCode();
        result = 31 * result + size.hashCode();
        result = 31 * result + drinker.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (int) (coffeeShopId ^ (coffeeShopId >>> 32));
        return result;
    }
}
