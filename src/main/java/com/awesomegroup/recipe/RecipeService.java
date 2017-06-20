package com.awesomegroup.recipe;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsService;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Michał on 2017-04-14.
 */
@Service
@Transactional //for future use with inserting data to db
public class RecipeService {

    public static final int RECIPES_PER_PAGE = 10;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientsService ingredientsService;

    public Iterable<Recipe> getAllRecipesPaged(PageRequest pageRequest) {
        return recipeRepository.findAllPaged(pageRequest);
    }

    public Iterable<Recipe> getByDifficulty(RecipeDifficulty recipeDifficulty, PageRequest pageRequest) {
        return recipeRepository.findByDifficulty(recipeDifficulty.getID(), pageRequest == null ? new PageRequest(1, RECIPES_PER_PAGE) : pageRequest);
    }
    public Iterable<Recipe> getByNamePaged(String name, PageRequest pageRequest) {
        return recipeRepository.findByNamePaged(name, pageRequest == null ? new PageRequest(1, RECIPES_PER_PAGE) : pageRequest);
    }

    public int getCount() {
        return (int) recipeRepository.count() ;
    }

    public int getByNameCount(String name) {
        return (int)recipeRepository.findByNameCount(name);
    }

    public Recipe getRecipeByName(String name) {
        return recipeRepository.findRecipeByName(name.replace("-", " "));
    }

    public void saveRecipe(Recipe recipe) {
        Optional.ofNullable(recipe).ifPresent(r -> recipeRepository.save(r));
    }

    public void saveRecipeWithStringIngredients(RecipeWithStringIngredients newRecipeWithStringIngredients) {
        if(newRecipeWithStringIngredients != null) {
            Recipe recipe = Recipe.create()
                    .name(newRecipeWithStringIngredients.getName())
                    .preparationTime(newRecipeWithStringIngredients.getEstimatedPreparationTime())
                    .difficulty(newRecipeWithStringIngredients.getDifficulty())
                    .servings(newRecipeWithStringIngredients.getServingsCount())
                    .build();

            // handle recipe ingredients
            newRecipeWithStringIngredients.getRecipeIngredients().forEach(
                    (String ingredientName) ->{
                        Ingredient referencedIngredient = null;
                        Optional<Ingredient> foundIngredient = ingredientsService.findIngredientsByName(ingredientName);
                        if(foundIngredient.isPresent()) {
                            referencedIngredient = foundIngredient.get();
                        }else{
                            referencedIngredient = Ingredient.create()
                                    .name(ingredientName)
                                    .build();
                            ingredientsService.addIngredient(referencedIngredient);
                        }

                        if(referencedIngredient != null) {
                            RecipeIngredient recipeIngredient = RecipeIngredient.create()
                                    .recipe(recipe)
                                    .ingredient(referencedIngredient)
                                    .count(1)
                                    .build();
                            if(recipeIngredient != null) {
                                recipe.getRecipeIngredients().add(recipeIngredient);
                            }else{
                            }
                        }else{
                            throw new CouldNotCreateRecipeIngredientException("Failed to create recipe-ingredient relation!");
                        }
                    }
            );
            recipeRepository.save(recipe);
        }
    }

    public void removeRecipe(long id) {
        recipeRepository.delete(id);
    }
}
