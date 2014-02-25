package com.mechanitis.demo.coffee;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongojack.Id;

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
    private final String[] selectedOptions;
    private final DrinkType type;
    private final String size;
    private final String drinker;
    @Id
    private ObjectId id;

    public Order(@JsonProperty("selectedOptions") final String[] selectedOptions,
                 @JsonProperty("type") final DrinkType type,
                 @JsonProperty("size") final String size,
                 @JsonProperty("drinker") final String drinker) {
        this.selectedOptions = selectedOptions;
        this.type = type;
        this.size = size;
        this.drinker = drinker;
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

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }
}
