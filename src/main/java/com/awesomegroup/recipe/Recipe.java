package com.awesomegroup.recipe;

import com.awesomegroup.fridge.favourite.FavouriteRecipe;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michał on 2017-04-14.
 */
@Entity
@Table(name = "recipes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false, unique = true, updatable = false)
    @JsonProperty("id")
    private long recipeID;

    @Column(name = "recipe_name", nullable = false, unique = true, length = 300)
    private String name;

    @Column(name = "recipe_preparation_time")
    @JsonProperty("prepTime")
    private short estimatedPreparationTime;

    @Column(name = "recipe_difficulty", nullable = false)
    private RecipeDifficulty difficulty;

    @Column(name = "recipe_servings", nullable = false)
    @JsonProperty("servings")
    private byte servingsCount;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL})
    @JsonIgnoreProperties("recipe")
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "recipe")
    private FavouriteRecipe favouriteRecipe;

    public static Recipe.Builder create() {
        return new Recipe.Builder();
    }

    public static Recipe.Builder create(Recipe recipe) {
        return new Recipe.Builder(recipe);
    }

    public long getRecipeID() {
        return recipeID;
    }

    public short getEstimatedPreparationTime() {
        return estimatedPreparationTime;
    }

    public String getName() {
        return name;
    }

    public RecipeDifficulty getDifficulty() {
        return difficulty;
    }

    public byte getServingsCount() {
        return servingsCount;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public FavouriteRecipe getFavouriteRecipe() {
        return favouriteRecipe;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeID=" + recipeID +
                ", estimatedPreparationTime=" + estimatedPreparationTime +
                ", name='" + name + '\'' +
                ", difficulty=" + difficulty +
                ", servingsCount=" + servingsCount +
                '}';
    }


    public static class Builder {

        private Recipe recipe;

        public Builder() {
            recipe = new Recipe();
        }

        public Builder(Recipe recipe) {
            this.recipe = recipe;
        }

        public Builder id(long id) {
            recipe.recipeID = id;
            return this;
        }

        public Builder preparationTime(short prepTime) {
            recipe.estimatedPreparationTime = prepTime;
            return this;
        }

        public Builder name(String name) {
            recipe.name = name;
            return this;
        }

        public Builder servings(byte servings) {
            recipe.servingsCount = servings;
            return this;
        }

        public Builder difficulty(RecipeDifficulty difficulty) {
            recipe.difficulty = difficulty;
            return this;
        }

        public Builder ingredients(RecipeIngredient... ingredients) {
            recipe.recipeIngredients.clear();
            recipe.recipeIngredients.addAll(Arrays.asList(ingredients));
            return this;
        }

        public Recipe build() {
            return recipe;
        }
    }
}
