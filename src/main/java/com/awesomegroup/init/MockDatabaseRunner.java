package com.awesomegroup.init;

import com.awesomegroup.food2fork.F2FRecipeRecipe;
import com.awesomegroup.food2fork.F2FSearchRecipe;
import com.awesomegroup.food2fork.F2FSearchResult;
import com.awesomegroup.food2fork.Food2fork;
import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientRepository;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeDifficulty;
import com.awesomegroup.recipe.RecipeRepository;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-04-17.
 */
//@Component
public class MockDatabaseRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MockDatabaseRunner.class);

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final Food2fork f2f;
    private final Random random;

//    @Autowired
    public MockDatabaseRunner(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, Food2fork f2f, Random random) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.f2f = f2f;
        this.random = random;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        F2FSearchResult topRated = f2f.findTopRated();

        for(F2FSearchRecipe recipe : topRated.getRecipes()) {
            log.info("----------------------------------- NEW RECIPE -----------------------------------");
            F2FRecipeRecipe r = f2f.getRecipe(recipe.getRecipeId());

            Recipe dbBean = Recipe.create()
                    .name(r.getRecipee().getTitle())
                    .preparationTime((short) random.nextInt(100))
                    .difficulty(RecipeDifficulty.findByID(random.nextInt(3)))
                    .servings((byte) random.nextInt(5))
                    .build();

            log.info(r.getRecipee().getIngridients().stream().collect(Collectors.joining(", ")));


            List<String> uniqueIngredients = r.getRecipee().getIngridients().stream()
                    .filter(s-> !s.contains("Note"))
                    .map(s->!s.contains("(") ? s : s.replace(s.substring(s.indexOf("("), s.indexOf(")")), ""))
                    .flatMap(Pattern.compile(" ")::splitAsStream)
                    .filter(s->!StringUtils.containsAny(s.toLowerCase(),"in", "of", "shredded", "lengthwise", "seeded", "half", "and", "slice", "cut", "room", "temperature", "crumbled", ")"))
                    .filter(s->!Pattern.matches("\\d", s))
                    .filter(s->!Pattern.matches("([cup]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("([pound]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("([quart]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("([spoon]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("([tablespoon]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("([gram]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("([milligram]+?)(s\\b|\\b)", s))
                    .filter(s->!Pattern.matches("(\\d)([.*])(\\d)",s))
                    .distinct().collect(Collectors.toList());

            log.info(uniqueIngredients.stream().collect(Collectors.joining(", ")));


            List<RecipeIngredient> recipeIngredients = uniqueIngredients.stream().filter(Objects::nonNull).map(ingredientName->{
                Optional<Ingredient> optionalIngredient = ingredientRepository.findIngredientsByName(ingredientName);
                Ingredient newIngredient = optionalIngredient.orElseGet(() -> Ingredient.create().name(ingredientName)
                        .build());

                log.info("Adding " + newIngredient.getIngredientName() + " id = " + newIngredient.getIngredientID());

                return RecipeIngredient.create()
                        .recipe(dbBean)
                        .ingredient(newIngredient)
                        .count(200).build();
            }).collect(Collectors.toList());

            dbBean.getRecipeIngredients().addAll(recipeIngredients);
            recipeRepository.save(dbBean);
        }

        log.info(topRated.toString());
    }
}
