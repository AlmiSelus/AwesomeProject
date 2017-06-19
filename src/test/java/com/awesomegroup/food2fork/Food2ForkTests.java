package com.awesomegroup.food2fork;

import com.awesomegroup.ingredients.Ingredient;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by patry on 19.06.2017.
 */
public class Food2ForkTests {

    Food2fork f2f = new Food2fork();

    @Test
    public void food2Fork_searchRecipesArgs()
    {
        F2FSearchResult f2FSearchResult = f2f.searchRecipes("chicken", 'r', 1);
        Assert.assertNotNull(f2FSearchResult);
        Assert.assertThat(f2FSearchResult.getCount(), is(30));
    }

    @Test
    public void food2Fork_searchRecipes()
    {
        F2FSearchResult f2FSearchResult = f2f.searchRecipes("chicken");
        Assert.assertNotNull(f2FSearchResult);
        Assert.assertThat(f2FSearchResult.getCount(), is(30));
    }

    @Test
    public void food2Fork_getRecipe()
    {
        F2FRecipeRecipe recipe = f2f.getRecipe("35120");
        Assert.assertNotNull(recipe);
        Assert.assertNotNull(recipe.getRecipee());
        Assert.assertThat(recipe.getRecipee().getTitle(), is("Bacon Wrapped Jalapeno Popper Stuffed Chicken"));
    }



}
