package com.awesomegroup.recipe;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michał on 2017-04-15.
 */
public class RecipeBuilderTests {

    @Test
    public void createRecipeWithBuilder_allValuesNotSet() {
        Recipe recipe = Recipe.create().build();
        Assert.assertNotNull(recipe);
        Assert.assertEquals("Recipe{recipeID=0, estimatedPreparationTime=0, name='null', difficulty=null, servingsCount=0}",
                recipe.toString());
    }

    @Test
    public void createRecipeWithBuilder_allValuesSetCorrectly() {
        Recipe recipe = Recipe.create()
                                .id(1)
                                .name("Test Name")
                                .preparationTime((short) 10)
                                .servings((byte) 1)
                                .difficulty(RecipeDifficulty.EASY)
                                .build();
        Assert.assertNotNull(recipe);
        Assert.assertEquals("Recipe{recipeID=1, estimatedPreparationTime=10, name='Test Name', difficulty=EASY, servingsCount=1}",
                recipe.toString());
    }

    @Test
    public void createRecipeWithBuilder_addedIngredients_shouldReturnAllWhenRequested() {
        //create ingredient
        Ingredient ingredient = Ingredient.create().name("Test ingredient").build();
        Recipe recipeBase = Recipe.create().name("Recipe").build();
        RecipeIngredient recipeIngredient = RecipeIngredient.create().ingredient(ingredient).recipe(recipeBase).build();

        //create recipe
        Recipe recipe = Recipe.create(recipeBase).ingredients(recipeIngredient).build();

        Assert.assertFalse(recipe.getRecipeIngredients().isEmpty());
        Assert.assertEquals(1, recipe.getRecipeIngredients().size());
    }

}
