package com.awesomegroup.init;

import com.awesomegroup.food2fork.F2FRecipeRecipe;
import com.awesomegroup.food2fork.F2FSearchRecipe;
import com.awesomegroup.food2fork.F2FSearchResult;
import com.awesomegroup.food2fork.Food2fork;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientMeasurement;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeDifficulty;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import com.awesomegroup.recipe.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-04-17.
 */
@Component
public class MockDatabaseRunner implements ApplicationRunner {

    private final static Logger log = LoggerFactory.getLogger(MockDatabaseRunner.class);

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Autowired
    private Food2fork f2f;

    @Autowired
    private Random random;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        Food2fork f2f = new Food2fork("716ca2be43d30cce65e497b5f7ef920e");
        //String result = f2f.searchRecipes("cheese,mushrooms");
        F2FRecipeRecipe result = f2f.getRecipe("35120");
        F2FSearchResult topRated = f2f.findTopRated();

        for(F2FSearchRecipe recipe : topRated.recipes) {
            log.info("----------------------------------- NEW RECIPE -----------------------------------");
            F2FRecipeRecipe r = f2f.getRecipe(recipe.recipe_id);

            Recipe dbBean = Recipe.create()
                    .name(r.recipe.title)
                    .preparationTime((short) random.nextInt(100))
                    .difficulty(RecipeDifficulty.findByID(random.nextInt(3)))
                    .servings((byte) random.nextInt(5))
                    .build();

            List<String> uniqueIngredients = r.recipe.ingredients.stream().distinct().collect(Collectors.toList());


            List<RecipeIngredient> recipeIngredients = uniqueIngredients.stream().filter(Objects::nonNull).map(ingredientName->{
                Optional<Ingredient> optionalIngredient = ingredientsRepository.findByName(ingredientName);
                Ingredient newIngredient = optionalIngredient.orElseGet(() -> Ingredient.create().name(ingredientName)
                        .availableMeasurements(IngredientMeasurement.ALL).build());

                log.info("Adding " + newIngredient.getIngredientName() + " id = " + newIngredient.getIngredientID());

                return RecipeIngredient.create()
                        .recipe(dbBean)
                        .ingredient(newIngredient)
                        .measurement(IngredientMeasurement.ML)
                        .count(200).build();
            }).collect(Collectors.toList());

            dbBean.getRecipeIngredients().addAll(recipeIngredients);
            recipeRepository.save(dbBean);
        }

        log.info(topRated.toString());
    }
}
