package com.awesomegroup.ingredients;

import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-04-18.
 */
@Entity
@Table(name = "ingredients")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ingredient {

    @Id
    @GeneratedValue
    @Column(name = "ingredient_id", nullable = false, unique = true, updatable = false)
    @JsonProperty("id")
    private long ingredientID;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    @JsonProperty("name")
    private String ingredientName;

    @ElementCollection(targetClass = IngredientMeasurement.class)
    @CollectionTable(name="ingredients_measurements")
    @Column(name = "ingredient_available_measurements")
    private List<IngredientMeasurement> availableMeasurements = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<RecipeIngredient> recipeIngredients;

    public long getIngredientID() {
        return ingredientID;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public List<IngredientMeasurement> getAvailableMeasurements() {
        return availableMeasurements;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientID=" + ingredientID +
                ", ingredientName='" + ingredientName + '\'' +
                ", availableMeasurements='" + availableMeasurements.stream().map(IngredientMeasurement::toString).collect(Collectors.joining(", ")) +"'"+
                '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private Ingredient ingredient = new Ingredient();

        public Builder id(long id) {
            ingredient.ingredientID = id;
            return this;
        }

        public Builder name(String name) {
            ingredient.ingredientName = name;
            return this;
        }

        public Builder availableMeasurements(IngredientMeasurement... measurements) {
            ingredient.availableMeasurements.addAll(Arrays.asList(measurements));
            return this;
        }

        public Ingredient build() {
            return ingredient;
        }
    }
}
