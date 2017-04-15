package com.awesomegroup.recipe;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michał on 2017-04-15.
 */
public class RecipeDifficultyTests {

    @Test
    public void callFindByName_lowercase_shouldReturnEASYEnum() {
        RecipeDifficulty recipeDifficulty = RecipeDifficulty.findByName("easy");
        Assert.assertNotNull(recipeDifficulty);
        Assert.assertEquals(RecipeDifficulty.EASY, recipeDifficulty);
    }

    @Test
    public void callFindByName_uppercase_shouldReturnEASYEnum() {
        RecipeDifficulty recipeDifficulty = RecipeDifficulty.findByName("EASY");
        Assert.assertNotNull(recipeDifficulty);
        Assert.assertEquals(RecipeDifficulty.EASY, recipeDifficulty);
    }

}
