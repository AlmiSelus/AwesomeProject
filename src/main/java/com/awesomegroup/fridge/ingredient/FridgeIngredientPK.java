package com.awesomegroup.fridge.ingredient;

import java.io.Serializable;

/**
 * Created by Michał on 2017-06-15.
 */
public class FridgeIngredientPK implements Serializable {

    private long fridge;
    private long ingredient;

    public long getFridge() {
        return fridge;
    }

    public long getIngredient() {
        return ingredient;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FridgeIngredientPK that = (FridgeIngredientPK) o;

        return !(fridge != that.fridge || ingredient != that.ingredient);
    }

    public int hashCode() {
        return 31 * Long.hashCode(fridge) + Long.hashCode(ingredient);
    }
}
