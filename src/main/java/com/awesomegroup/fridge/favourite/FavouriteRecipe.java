package com.awesomegroup.fridge.favourite;

import com.awesomegroup.fridge.Fridge;
import com.awesomegroup.recipe.Recipe;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adi on 04.06.2017.
 */
@Entity
@Table(name = "favourite_recipes")
@JsonSerialize
public class FavouriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favourite_recipe_id", unique = true, nullable = false)
    @JsonProperty("id")
    private long favouriteRecipeID;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "recipe_rating", nullable = false, unique = true, length = 300)
    private float rating;

    @ManyToMany(mappedBy = "favouriteRecipes")
    private List<Fridge> fridges = new ArrayList<>();

    public List<Fridge> getFridges() {
        return fridges;
    }

    public void addFridge(Fridge fridge) {
        fridges.add(fridge);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getFavouriteRecipeID() {
        return favouriteRecipeID;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private FavouriteRecipe favouriteRecipe;

        private Builder() {
            favouriteRecipe = new FavouriteRecipe();
        }

        public Builder id(long id) {
            favouriteRecipe.favouriteRecipeID = id;
            return this;
        }

        public Builder rating(float rating) {
            favouriteRecipe.rating = rating;
            return this;
        }

        public Builder recipe(Recipe recipe) {
            favouriteRecipe.recipe = recipe;
            return this;
        }

        public FavouriteRecipe build() {
            return favouriteRecipe;
        }

    }


}
