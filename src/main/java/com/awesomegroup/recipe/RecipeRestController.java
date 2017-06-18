package com.awesomegroup.recipe;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsService;
import com.awesomegroup.recipeingredient.RecipeIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michał on 2017-04-14.
 */
@RestController
public class RecipeRestController {

    private final RecipeService recipeService;
    private final IngredientsService ingredientsService;

    @Autowired
    public RecipeRestController(RecipeService recipeService, IngredientsService ingredientsService) {
        this.recipeService = recipeService;
        this.ingredientsService = ingredientsService;
    }

    @GetMapping("/api/recipe-{page}")
    public Iterable<Recipe> findAll(@PathVariable("page") int page) {
        return recipeService.getAllRecipesPaged(new PageRequest(page, RecipeService.RECIPES_PER_PAGE));
    }

    @GetMapping("/api/recipe/difficulty-{diff}/{page}")
    public Iterable<Recipe> findAllByDifficulty(@PathVariable("diff") String difficulty, @PathVariable("page") int page) {
        RecipeDifficulty recipeDifficulty = RecipeDifficulty.findByName(difficulty);
        return recipeService.getByDifficulty(recipeDifficulty, new PageRequest(page, RecipeService.RECIPES_PER_PAGE));
    }

    @GetMapping("/api/recipe/{name}")
    public Recipe getRecipeByName(@PathVariable("name") String name) {
        return recipeService.getRecipeByName(name);
    }

    @PostMapping(value = "/api/recipe/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeAddResult saveRecipe(@RequestBody Recipe newRecipe) {
        RecipeAddResult recipeAddResult = new RecipeAddResult();
        if(newRecipe != null) {
            try {
                if(recipeService.getRecipeByName(newRecipe.getName()) == null) {
                    recipeService.saveRecipe(newRecipe);
                }else{
                    recipeAddResult.success = false;
                    recipeAddResult.message = "There is already a recipe with given name!";
                }
            }
            catch(DataIntegrityViolationException e) {
                recipeAddResult.success = false;
                recipeAddResult.message = e.getCause().getCause().getMessage();
            }
            catch(Exception e) {
                recipeAddResult.success = false;
                recipeAddResult.message = e.getMessage();
            }
        }else{
            recipeAddResult.success = false;
            recipeAddResult.message = "Recived null receipt";
        }
        return recipeAddResult;
    }

    @PostMapping(value = "/api/recipe/addWithStringIngredients", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeAddResult saveRecipeWithStringIngredients(@RequestBody RecipeWithStringIngredients newRecipeWithStringIngredients) {
        RecipeAddResult recipeAddResult = new RecipeAddResult();
        if(newRecipeWithStringIngredients != null) {
            try {
                if(recipeService.getRecipeByName(newRecipeWithStringIngredients.getName()) == null) {
                    recipeService.saveRecipeWithStringIngredients(newRecipeWithStringIngredients);

                }else{
                    recipeAddResult.success = false;
                    recipeAddResult.message = "There is already a recipe with given name!";
                }
            }
            catch(DataIntegrityViolationException e) {
                recipeAddResult.success = false;
                recipeAddResult.message = e.getCause().getMessage();
            }
            catch(Exception e) {
                recipeAddResult.success = false;
                recipeAddResult.message = e.getMessage();
            }
        }else{
            recipeAddResult.success = false;
            recipeAddResult.message = "Recived null receipt";
        }
        return recipeAddResult;
    }

    @PostMapping("api/recipe/log")
    public void logToConsole(@RequestBody String content) {
        System.out.println(content);
    }

    @DeleteMapping("api/recipe/delete/{id}")
    public void deleteRecipe(@PathVariable("id") long id) {
        recipeService.RemoveRecipe(id);
    }
}
