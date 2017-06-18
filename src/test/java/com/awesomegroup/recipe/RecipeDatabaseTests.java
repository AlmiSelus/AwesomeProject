package com.awesomegroup.recipe;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by c309044 on 2017-05-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@Transactional
public class RecipeDatabaseTests {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientsRepository ingredientRepository;

    @Test
    @DatabaseSetup("/database/recipe4Entries.xml")
    public void callFindAll_return4Recipes() {
        List<Recipe> recipes = (List<Recipe>)recipeRepository.findAll();
        Assert.assertEquals(4, recipes.size());
    }

    @Test
    @DatabaseSetup("/database/emptyDB.xml")
    public void testAddRecipeToDB() {

        Ingredient ingredient0 = Ingredient.create()
                .name("Sugar")
                .build();
        ingredientRepository.save(ingredient0);

        Ingredient ingredient1 = Ingredient.create()
                .name("game")
                .build();
        ingredientRepository.save(ingredient1);

        Recipe pancakesRecipe = Recipe.create()
                .name("Pancakes")
                .preparationTime((short)15)
                .difficulty(RecipeDifficulty.EASY)
                .servings((byte)2)
                .build();

        pancakesRecipe.getRecipeIngredients().add(RecipeIngredient.create().recipe(pancakesRecipe).ingredient(ingredient0).build());
        pancakesRecipe.getRecipeIngredients().add(RecipeIngredient.create().recipe(pancakesRecipe).ingredient(ingredient1).build());

        recipeRepository.save(pancakesRecipe);

        Recipe rediscoveredRecipe = recipeRepository.findRecipeByName(pancakesRecipe.getName());

        Assert.assertNotNull(rediscoveredRecipe);

        Assert.assertEquals(pancakesRecipe.getName(), rediscoveredRecipe.getName());

        Assert.assertEquals(pancakesRecipe.getRecipeIngredients().size(), rediscoveredRecipe.getRecipeIngredients().size());
    }

    @Test
    @DatabaseSetup("/database/recipe4Entries.xml")
    public void testRemoveRecipeFromDB() {
        Recipe recipeByName = recipeRepository.findRecipeByName("Recipe Name");

        Assert.assertNotNull(recipeByName);

        recipeRepository.delete(recipeByName.getRecipeID());

        Recipe recipeByName2 = recipeRepository.findRecipeByName("Recipe Name");

        Assert.assertNull(recipeByName2);
    }
}
