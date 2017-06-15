package com.awesomegroup.fridgeIngredient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by patry on 15.06.2017.
 */
public class FridgeIngredientJson {
    @JsonProperty("ingredient_name")
    private String ingredientName;
    @JsonProperty("ingredient_expire_date")
    private String ingredientExpireDate;

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientExpireDate() {
        return ingredientExpireDate;
    }

    public void setIngredientExpireDate(String ingredientExpireDate) {
        this.ingredientExpireDate = ingredientExpireDate;
    }
}
