package com.awesomegroup.recipe;

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
        Assert.assertEquals("Recipe{recipeID=0, estimatedPreparationTime=0, name='null', difficulty=0, servingsCount=0}",
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
        Assert.assertEquals("Recipe{recipeID=1, estimatedPreparationTime=10, name='Test Name', difficulty=0, servingsCount=1}",
                recipe.toString());
    }

}
