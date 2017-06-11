package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipe.Recipe;
import org.junit.Assert;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Adi on 04.06.2017.
 */
public class FridgeTests {

    @Test
    public void fridge_findIngredient() {
        Fridge fridge = Fridge.create().build();
        Ingredient ingredient = Ingredient.create().id(3).build();
        fridge.addIngredient(ingredient);
        Assert.assertThat(fridge.findIngredientByID(ingredient), is(0));
        Assert.assertThat(fridge.findIngredientByID(Ingredient.create().id(4).build()), is(-1));
    }

    @Test
    public void fridge_isIngredientPresent() {
        Fridge fridge = Fridge.create().build();
        fridge.addIngredient(Ingredient.create().id(3).build());
        Assert.assertThat(fridge.isIngredientPresent(Ingredient.create().id(3).build()), is(true));
        Assert.assertThat(fridge.isIngredientPresent(Ingredient.create().id(4).build()), is(false));
    }

    @Test
    public void fridge_addFridgeIngredient() {
        Fridge fridge = Fridge.create().build();
        fridge.addIngredient(Ingredient.create().id(3).build());
        fridge.addIngredient(Ingredient.create().id(3).build());
        Assert.assertThat(fridge.getFridgeIngredients().isEmpty(), is(false));
        Assert.assertThat(fridge.getFridgeIngredients().size(), is(1));
    }

    @Test
    public void fridge_removeFridgeIngredient() {
        Fridge fridge = Fridge.create().build();
        fridge.addIngredient(Ingredient.create().id(4).build());
        fridge.addIngredient(Ingredient.create().id(5).build());
        fridge.addIngredient(Ingredient.create().id(1).build());
        fridge.addIngredient(Ingredient.create().id(3).build());
        fridge.addIngredient(Ingredient.create().id(6).build());

        Ingredient ingredient = Ingredient.create().id(3).build();
        Assert.assertThat(fridge.isIngredientPresent(ingredient), is(true));
        fridge.removeIngredient(ingredient);
        Assert.assertThat(fridge.isIngredientPresent(ingredient), is(false));
    }

    @Test
    public void fridge_isFavouriteRecipePresent()
    {
        Fridge fridge = Fridge.create().build();
        Recipe recipe = Recipe.create().id(1).build();
        Assert.assertThat(fridge.isFavouriteRecipePresent(recipe), is(false) );
        fridge.addFavouriteRecipe(recipe,0);
        Assert.assertThat(fridge.isFavouriteRecipePresent(recipe), is(true) );
    }

    @Test
    public void fridge_addFavouriteRecipe() {
        Recipe recipe = Recipe.create().id(3).build();
        //add
        Fridge fridge = Fridge.create().build();
        fridge.addFavouriteRecipe(recipe, 0);
        fridge.addFavouriteRecipe(recipe, 0);
        fridge.addFavouriteRecipe(recipe, 0);
        Assert.assertThat(fridge.getFavouriteRecipes().size(), is(1));
    }

    @Test
    public void fridge_findFavouriteRecipe()
    {
        Fridge fridge = Fridge.create().build();
        Recipe recipe = Recipe.create().id(1).build();

        Assert.assertThat(fridge.findFavouriteRecipe(recipe), is(-1));
        fridge.addFavouriteRecipe(recipe, 1);
        Assert.assertThat(fridge.findFavouriteRecipe(recipe), is(0));
    }

    @Test
    public void fridge_removeFavouriteRecipe() {
        Fridge fridge = Fridge.create().build();
        Recipe recipe = Recipe.create().id(3).build();
        fridge.addFavouriteRecipe(recipe, 0);
        fridge.removeFavouriteRecipe(recipe);
        Assert.assertThat(fridge.isFavouriteRecipePresent(recipe), is(false));

    }


    @Test
    public void fridge_rateFavouriteRecipe() {
        Fridge fridge = Fridge.create().build();

        Recipe recipe = Recipe.create().id(3).build();
        fridge.addFavouriteRecipe(recipe, 0);
        Assert.assertThat(fridge.getFavouriteRecipeRating(recipe), is(0f));

        fridge.rateFavouriteRecipe(recipe, 3);
        Assert.assertThat(fridge.getFavouriteRecipeRating(recipe), is(3f));
    }

    @Test
    public void recipeListByRating() {
        Fridge fridge = Fridge.create().build();

        Recipe recipe = Recipe.create().id(3).build();
        fridge.addFavouriteRecipe(recipe, 0);
        fridge.addFavouriteRecipe(Recipe.create().id(4).build(), 0);
        fridge.addFavouriteRecipe(Recipe.create().id(5).build(), 0);
        fridge.addFavouriteRecipe(Recipe.create().id(6).build(), 1);

        Assert.assertThat(fridge.getRecipesOfRating(2).size(), is(0));
        Assert.assertThat(fridge.getRecipesOfRating(1).size(), is(1));
        Assert.assertThat(fridge.getRecipesOfRating(0).size(), is(3));
    }

}
