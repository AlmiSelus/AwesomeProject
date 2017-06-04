package com.awesomegroup.fridge;

import com.awesomegroup.favouriteRecipe.FavouriteRecipe;
import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Adi on 26.05.2017.
 */

@Entity
@Table(name = "fridges")
@JsonSerialize
public class Fridge {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_id", unique = true, nullable = false)
    @JsonProperty("id")
    private long fridgeId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fridge")
    private User fridgeUser;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "fridge_ingredients",
            joinColumns = @JoinColumn(name = "fridge_id", referencedColumnName = "fridge_id"),  //name = nowa kolumna, ref = istenijacy klucz
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    )
    private List<Ingredient> fridgeIngredients = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "fridge_favourite_recipes",
            joinColumns = @JoinColumn(name = "fridge_id", referencedColumnName = "fridge_id"),  //name = nowa kolumna, ref = istenijacy klucz
            inverseJoinColumns = @JoinColumn(name = "favourite_recipe_id", referencedColumnName = "favourite_recipe_id")
    )
    private List<FavouriteRecipe> favouriteRecipes = new ArrayList<>();

    public long getFridgeId() {
        return fridgeId;
    }

    public User getFridgeUser() {
        return fridgeUser;
    }

    public List<Ingredient> getFridgeIngredients() {
        return fridgeIngredients;
    }

    public List<FavouriteRecipe> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    public int findIngredientByID(Ingredient ingredient) {
        int index = -1;
        if (isIngredientPresent(ingredient))
            for (index = 0; index < fridgeIngredients.size(); index++) {
                if (fridgeIngredients.get(index).getIngredientID() == ingredient.getIngredientID()) {
                    break;
                }
            }
        return index;
    }

    public boolean isIngredientPresent(Ingredient ingredient) {
        return fridgeIngredients.stream()
                .filter(ingredient1 -> ingredient1.getIngredientID() == ingredient.getIngredientID())
                .count() > 0;
    }

    public void addIngredient(Ingredient ingredient) {
        if (!isIngredientPresent(ingredient)) fridgeIngredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        int index = findIngredientByID(ingredient);
        if (index != -1) ;
        fridgeIngredients.remove(index);
    }

    public boolean isFavouriteRecipePresent(Recipe recipe) {
        return favouriteRecipes.stream()
                .filter(r -> r.getRecipe().getRecipeID() == recipe.getRecipeID())
                .count() > 0;
    }

    public void addFavouriteRecipe(Recipe recipe, int rating) {
        if (!isFavouriteRecipePresent(recipe)) {
            favouriteRecipes.add(FavouriteRecipe.create().recipe(recipe).rating(rating).build());
        }
    }

    public int findFavouriteRecipe(Recipe recipe) {
        int index = -1;
        if (isFavouriteRecipePresent(recipe))
            for (index = 0; index < favouriteRecipes.size(); index++) {
                if (favouriteRecipes.get(index).getRecipe().getRecipeID() == recipe.getRecipeID()) {
                    break;
                }
            }
        return index;
    }

    public void removeFavouriteRecipe(Recipe recipe) {
        int index = findFavouriteRecipe(recipe);
        if (index != -1) ;
        favouriteRecipes.remove(index);
    }

    public float getFavouriteRecipeRating(Recipe recipe) {
        int index = findFavouriteRecipe(recipe);
        if (index != -1)
        return favouriteRecipes.get(index).getRating();
        return -1f;
    }

    public void rateFavouriteRecipe(Recipe recipe, int rating) {
        int index = findFavouriteRecipe(recipe);
        if (index != -1) ;
        favouriteRecipes.get(index).setRating(rating);


    }
 /*
    public List<Recipe> getRecipesOfRating(int rating) {
        return new ArrayList<>(favouriteRecipes.entrySet().stream()
                .filter(recipeIntegerEntry -> recipeIntegerEntry.getValue() == rating)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())).keySet());
    }




    */

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private Fridge fridge;

        private Builder() {
            fridge = new Fridge();
        }

        public Builder id(long id) {
            fridge.fridgeId = id;
            return this;
        }

        public Builder user(User user) {
            fridge.fridgeUser = user;
            return this;
        }

        public Fridge build() {
            return fridge;
        }

    }

}
