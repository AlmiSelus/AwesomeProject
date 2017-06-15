package com.awesomegroup.fridgeIngredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by patry on 15.06.2017.
 */
@JsonSerialize
@JsonDeserialize
public class FridgeIngredientJson {

    @JsonProperty("name")
    private String ingredientName;

    @JsonProperty("date")
    private String ingredientExpireDate;

    public String getIngredientName() {
        return ingredientName;
    }

    public String getIngredientExpireDate() {
        return ingredientExpireDate;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private FridgeIngredientJson ingredientJson;

        public Builder() {
            ingredientJson = new FridgeIngredientJson();
        }

        public Builder name(String name) {
            ingredientJson.ingredientName = name;
            return this;
        }

        public Builder expires(String expireDate) {
            ingredientJson.ingredientExpireDate = expireDate;
            return this;
        }

        public FridgeIngredientJson build() {
            return ingredientJson;
        }
    }
}
