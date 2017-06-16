package com.awesomegroup.ingredients;

import com.awesomegroup.fridgeIngredient.FridgeIngredient;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał on 2017-04-18.
 */
@Entity
@Table(name = "ingredients")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false, unique = true)
    @JsonProperty("id")
    private long ingredientID;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    @JsonProperty("name")
    private String ingredientName;

    @OneToMany(mappedBy = "ingredient", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("ingredient")
    @JsonIgnore
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient")
    private List<FridgeIngredient> fridges = new ArrayList<>();

    public long getIngredientID() {
        return ingredientID;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<FridgeIngredient> getFridges() {
        return fridges;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientID=" + ingredientID +
                ", ingredientName='" + ingredientName + '\'' +
//                ", expireDate='" + expireDate + '\'' +
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

        public Ingredient build() {
            return ingredient;
        }
    }
}
