package com.awesomegroup.favouriteRecipe;

import com.awesomegroup.fridge.Fridge;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * Created by Adi on 06.06.2017.
 */
public class FavouriteRecipeTests {

    @Test
    public void favouriteRecipe_getFridges(){
        FavouriteRecipe favRecipe = FavouriteRecipe.create().build();
        favRecipe.addFridge(Fridge.create().build());
        Assert.assertThat(favRecipe.getFridges().isEmpty(), is(false));

    }
}
