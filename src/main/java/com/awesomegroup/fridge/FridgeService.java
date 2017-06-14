package com.awesomegroup.fridge;

import com.awesomegroup.favouriteRecipe.FavouriteRecipe;
import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeRepository;
import com.awesomegroup.user.User;
import com.awesomegroup.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-06-04.
 */
@Service
@Transactional
public class FridgeService {

    private static final Logger log = LoggerFactory.getLogger(FridgeService.class);

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public boolean addFridgeIngredientsForUser(Principal principalUser, List<Ingredient> ingredients) {
        if(principalUser == null || ingredients == null || ingredients.isEmpty()) {
            return false;
        }

        log.info("Principal user mail = {}", principalUser.getName());

        return userRepository.findUserByEmail(principalUser.getName()).map(user->{
            Fridge fridge = user.getFridge();
            //fridge.getFridgeIngredients().clear();
            ingredients.stream()
                    .map(ingredient -> ingredientsRepository.findByName(ingredient.getIngredientName()))
                    .forEach(optionalIngredient -> optionalIngredient.ifPresent(fridge.getFridgeIngredients()::add));
            fridgeRepository.save(fridge);
            return true;
        }).orElse(false);
    }

    public boolean addFridgeIngredientForUser(Principal principalUser, Ingredient ingredient) {
        if(principalUser == null || ingredient == null ) {
            return false;
        }

        log.info("Principal user mail = {}", principalUser.getName());

        return userRepository.findUserByEmail(principalUser.getName()).map(user->{
            Fridge fridge = user.getFridge();
            Optional.of(ingredient).map(ingr -> ingredientsRepository.findByName(ingr.getIngredientName()))
                    .map(optionalIngredient -> {
                        optionalIngredient.ifPresent(fridge.getFridgeIngredients()::add);
                        return true;
                    });
            fridgeRepository.save(fridge);
            return true;
        }).orElse(false);
    }

    public List<Ingredient> getCurrentIngredients(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .map(user -> user.getFridge().getFridgeIngredients())
                .orElse(Collections.emptyList());
    }

    public boolean addRecipeToFavourites(Principal principal, Recipe recipe) {
        if(recipe == null) {
            return false;
        }

        return userRepository.findUserByEmail(principal.getName())
            .map(User::getFridge)
            .filter(fridge->!hasRecipeInFridgeFavourites(fridge, recipe))
            .map(fridge -> {
                Optional.ofNullable(recipeRepository.findRecipeByName(recipe.getName())).ifPresent(foundRecipe -> {
                    fridge.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(foundRecipe).rating(0).build());
                    fridgeRepository.save(fridge);
                });
                return true;
            }).orElse(false);
    }

    public boolean removeRecipeFromFavourites(Principal principal, Recipe recipe) {
        if(recipe == null) {
            return false;
        }

        return userRepository.findUserByEmail(principal.getName())
            .map(User::getFridge)
            .filter(fridge -> hasRecipeInFridgeFavourites(fridge, recipe))
            .map(fridge -> {
                Optional.ofNullable(recipeRepository.findRecipeByName(recipe.getName())).ifPresent(foundRecipe -> {
                    fridge.getFavouriteRecipes().removeIf(favouriteRecipe -> favouriteRecipe.getRecipe().getRecipeID() == recipe.getRecipeID());
                    fridgeRepository.save(fridge);
                });
                return true;
            }).orElse(false);
    }

    public List<Recipe> getAllRecipesOfRating(Principal principal, float rating) {
        return userRepository.findUserByEmail(principal.getName())
            .map(User::getFridge)
            .map(fridge -> fridge.getFavouriteRecipes().stream()
                    .filter(favouriteRecipe -> Float.compare(favouriteRecipe.getRating(), rating) == 0)
                    .map(FavouriteRecipe::getRecipe)
                    .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    public boolean changeRecipeRating(Principal principal, Recipe recipe, float rating) {
        if(recipe == null || rating < 0) {
            return false;
        }

        return userRepository.findUserByEmail(principal.getName()).map(user -> {
            Fridge fridge = user.getFridge();

            return hasRecipeInFridgeFavourites(fridge, recipe) && fridge.getFavouriteRecipes().stream()
                .filter(favouriteRecipe -> favouriteRecipe.getRecipe().getName().equals(recipe.getName()))
                .findFirst().map(favouriteRecipe -> {
                    favouriteRecipe.setRating(rating);
                    fridgeRepository.save(fridge);
                    return true;
                }).orElse(false);
        }).orElse(false);
    }

    private boolean hasRecipeInFridgeFavourites(Fridge fridge, Recipe recipe) {
        return fridge.getFavouriteRecipes().stream()
                .filter(favRecipe->favRecipe.getRecipe().getRecipeID() == recipe.getRecipeID())
                .count() > 0;

    }
}
