package com.awesomegroup.fridgeIngredient;

import com.awesomegroup.fridge.Fridge;
import com.awesomegroup.ingredients.Ingredient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Created by patry on 15.06.2017.
 */

@Entity
@Table(name = "fridge_ingredient")
@IdClass(FridgeIngredientPK.class)
public class FridgeIngredient {

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "fridge_ingredient_fridge", referencedColumnName = "fridge_id")
    @JsonIgnoreProperties("fridgeIngredients")
    private Fridge fridge;

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "fridge_ingredient_ingredient", referencedColumnName = "ingredient_id")
    @JsonIgnoreProperties("fridgeIngredients")
    private Ingredient ingredient;

    @Column(name = "fridge_ingredient_expire")
    private LocalDate expireDate;

    public Fridge getRecipe() {
        return fridge;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public static FridgeIngredient.Builder create() {
        return new FridgeIngredient.Builder();
    }

    public static class Builder {
        private FridgeIngredient fridgeIngredient = new FridgeIngredient();

        public FridgeIngredient.Builder fridge(Fridge fridge) {
            fridgeIngredient.fridge = fridge;
            return this;
        }

        public FridgeIngredient.Builder ingredient(Ingredient ingredient) {
            fridgeIngredient.ingredient = ingredient;
            return this;
        }

        public FridgeIngredient.Builder expires(LocalDate date) {
            fridgeIngredient.expireDate = date;
            return this;
        }

        public FridgeIngredient build() {
            return fridgeIngredient;
        }
    }
}