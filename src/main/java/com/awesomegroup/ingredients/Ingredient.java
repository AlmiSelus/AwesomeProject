package com.awesomegroup.ingredients;

import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false, unique = true)
    @JsonProperty("id")
    private long ingredientID;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    @JsonProperty("name")
    private String ingredientName;

    @ElementCollection(targetClass = IngredientMeasurement.class)
    @CollectionTable(name="ingredients_measurements")
    @Column(name = "ingredient_available_measurements")
    private List<IngredientMeasurement> availableMeasurements = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("ingredient")
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

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

    public void Update(Ingredient ingredient) {
        ingredient.getAvailableMeasurements().forEach(
                ingredientMeasurement -> {
                    if(!availableMeasurements.contains(ingredientMeasurement)) {
                        availableMeasurements.add(ingredientMeasurement);
                    }
                }
        );
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
