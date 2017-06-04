package com.awesomegroup.favouriteRecipe;

import com.awesomegroup.fridge.Fridge;
import com.awesomegroup.recipe.Recipe;
import org.junit.Test;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by Adi on 04.06.2017.
 */
public class FavouriteRecipeBuilderTests {

    @Test
    public void favouriteRecipeBuilder_createDefaultObject()
    {
        Assert.assertThat(FavouriteRecipe.create().build(), is(notNullValue()));
    }

    public void favouriteRecipeBuilder_createSpecificObject()
    {
        FavouriteRecipe favRecipe = FavouriteRecipe.create()
                .recipe(Recipe.create()
                        .id(2)
                        .build())
                .rating(2f)
                .build();

        Assert.assertThat(favRecipe.getRecipe().getRecipeID(), is(2));
        Assert.assertThat(favRecipe.getRating(), is(2f));
    }
}
