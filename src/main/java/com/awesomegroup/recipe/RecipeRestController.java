package com.awesomegroup.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * Created by Michał on 2017-04-14.
 */
@RestController
public class RecipeRestController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeRestController(RecipeService recipeService) {
        this.recipeService = recipeService;
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
                recipeService.saveRecipe(newRecipe);
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

    @PostMapping("api/recipe/log")
    public void logToConsole(@RequestBody String content) {
        System.out.println(content);
    }
}
