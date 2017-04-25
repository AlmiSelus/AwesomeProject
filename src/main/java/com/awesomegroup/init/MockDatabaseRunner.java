package com.awesomegroup.init;

import com.awesomegroup.food2fork.Food2fork;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientMeasurement;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeDifficulty;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.awesomegroup.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Michał on 2017-04-17.
 */
@Component
public class MockDatabaseRunner implements ApplicationRunner {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Ingredient ingredient = Ingredient.create()
                                            .id(1L)
                                            .availableMeasurements(IngredientMeasurement.ML, IngredientMeasurement.L)
                                            .name("milk")
                                            .build();
        Ingredient ingredient2 = Ingredient.create().id(2L).availableMeasurements(IngredientMeasurement.PINCH).name("pepper").build();

        Food2fork f2f = new Food2fork("716ca2be43d30cce65e497b5f7ef920e");
        //String result = f2f.searchRecipes("cheese,mushrooms");
        String result = f2f.getRecipe("35120");
        Recipe recipe = Recipe.create()
                                .id(1)
                                .name(result)
                                .preparationTime((short) 25)
                                .difficulty(RecipeDifficulty.EASY)
                                .servings((byte) 1)
                                .ingredients()
                                .build();

        RecipeIngredient recipeIngredient = RecipeIngredient.create()
                .recipe(recipe)
                .ingredient(ingredient)
                .measurement(IngredientMeasurement.ML)
                .count(200).build();

        recipe.getRecipeIngredients().add(recipeIngredient);
        recipe.getRecipeIngredients().add(RecipeIngredient.create().recipe(recipe).ingredient(ingredient2).measurement(IngredientMeasurement.PINCH).build());




        recipeRepository.save(recipe);

    }
}
