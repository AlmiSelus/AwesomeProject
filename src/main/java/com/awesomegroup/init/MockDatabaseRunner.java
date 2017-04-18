package com.awesomegroup.init;

import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeDifficulty;
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

        Recipe recipe = new Recipe.Builder()
                                .id(1)
                                .name("Recipe name")
                                .preparationTime((short) 25)
                                .difficulty(RecipeDifficulty.EASY)
                                .servings((byte) 1)
                                .build();
        recipeRepository.save(recipe);

    }
}
