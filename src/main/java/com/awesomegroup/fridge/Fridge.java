package com.awesomegroup.fridge;

import com.awesomegroup.fridge.favourite.FavouriteRecipe;
import com.awesomegroup.fridge.ingredient.FridgeIngredient;
import com.awesomegroup.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "fridge", cascade = {CascadeType.ALL})
    private List<FridgeIngredient> fridgeIngredients = new ArrayList<>();

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

    public List<FridgeIngredient> getFridgeIngredients() {
        return fridgeIngredients;
    }

    public List<FavouriteRecipe> getFavouriteRecipes() {
        return favouriteRecipes;
    }

    @Override
    public String toString() {
        return "Fridge{" +
                "fridgeId=" + fridgeId +
                ", fridgeUser=" + fridgeUser +
                ", fridgeIngredients=" + fridgeIngredients +
                ", favouriteRecipes=" + favouriteRecipes +
                '}';
    }

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
