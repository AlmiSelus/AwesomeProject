package com.awesomegroup.ingredients;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by c309044 on 2017-04-18.
 */
public class IngredientBuilderTests {

    @Test
    public void createIngredientWithBuilder_allValuesNotSet() {
        Ingredient ingredient = Ingredient.create().build();
        Assert.assertNotNull(ingredient);
//        Assert.assertEquals("Ingredient{ingredientID=0, ingredientName='null', expireDate='null'}", ingredient.toString());
        Assert.assertEquals("Ingredient{ingredientID=0, ingredientName='null'}", ingredient.toString());
    }

    @Test
    public void createIngredientWithBuilder_allValuesSet() {
        Ingredient ingredient = Ingredient.create().id(1L).name("Milk").build();
        Assert.assertNotNull(ingredient);
//        String expected = "Ingredient{ingredientID=1, ingredientName='Milk', expireDate='null'}";
        String expected = "Ingredient{ingredientID=1, ingredientName='Milk'}";
        Assert.assertEquals(expected, ingredient.toString());
    }

}
