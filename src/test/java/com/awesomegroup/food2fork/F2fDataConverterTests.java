package com.awesomegroup.food2fork;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import org.junit.Test;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by patry on 19.06.2017.
 */
public class F2fDataConverterTests {

    @Test
    public void f2fDataConverter_convertIngredient()
    {
        Ingredient ingredient = F2fDataConverter.convertIngredient("chicken");
        Assert.assertThat(ingredient.getIngredientID(), is(0L));
        Assert.assertThat(ingredient.getIngredientName(), is("chicken"));
    }

    @Test
    public void f2fDataConverter_convertRecipeIngredient()
    {
        Ingredient ingredient = F2fDataConverter.convertIngredient("chicken");
        RecipeIngredient recipeIngredient = F2fDataConverter.convertRecipeIngredient(ingredient , 0);
        Assert.assertThat(recipeIngredient.getIngredient(), is(ingredient));
        Assert.assertThat(recipeIngredient.getCount(), is(0));
    }

    @Test
    public void f2fDataConverter_convertRecipe()
    {
        Food2fork f2f = new Food2fork();
        Recipe recipe = F2fDataConverter.convertRecipe(f2f.getRecipe("35120").getRecipee());
        Assert.assertThat(recipe.getName(), is("Bacon Wrapped Jalapeno Popper Stuffed Chicken"));
        Assert.assertThat(recipe.getRecipeIngredients().size(), is (6));
        Assert.assertThat(recipe.getServingsCount(), is(Byte.parseByte("0")));
        Assert.assertThat(recipe.getEstimatedPreparationTime(), is(Short.parseShort("0")));
    }
}
