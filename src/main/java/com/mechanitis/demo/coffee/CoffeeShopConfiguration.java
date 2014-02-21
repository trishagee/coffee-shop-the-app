package com.mechanitis.demo.coffee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class CoffeeShopConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String assetsPath;

    public String getAssetsPath() {
        return assetsPath;
    }
}