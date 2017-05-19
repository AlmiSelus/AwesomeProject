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

    @Test
    public void callFindByID_shouldReturnEasy() {
        RecipeDifficulty easy = RecipeDifficulty.findByID(0);
        Assert.assertNotNull(easy);
        Assert.assertEquals(RecipeDifficulty.EASY, easy);
    }

    @Test
    public void callGetID_shouldReturn0() {
        Assert.assertEquals(0, RecipeDifficulty.EASY.getID());
    }

    @Test
    public void callFindByID_shouldReturnNull() {
        Assert.assertNull(RecipeDifficulty.findByID(-1));
    }

}
