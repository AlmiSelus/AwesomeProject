package com.awesomegroup.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wrobe on 16.06.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeWithStringIngredients {

    @JsonProperty("recipeID")
    private long recipeID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("estimatedPreparationTime")
    private short estimatedPreparationTime;

    @JsonProperty("difficulty")
    private RecipeDifficulty difficulty;

    @JsonProperty("servingsCount")
    private byte servingsCount;

    @JsonProperty("recipeIngredients")
    private List<String> recipeIngredients = new ArrayList<>();

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

    public List<String> getRecipeIngredients() {
        return recipeIngredients;
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

        private RecipeWithStringIngredients recipe;

        public Builder() {
            recipe = new RecipeWithStringIngredients();
        }

        public Builder(RecipeWithStringIngredients recipe) {
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

        public Builder ingredients(String... ingredients) {
            recipe.recipeIngredients.clear();
            recipe.recipeIngredients.addAll(Arrays.asList(ingredients));
            return this;
        }

        public RecipeWithStringIngredients build() {
            return recipe;
        }
    }
}
