package com.awesomegroup.recipe;

import com.awesomegroup.food2fork.F2fDataConverter;
import com.awesomegroup.food2fork.Food2fork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/recipe-count")
    public int getRecipeCount() {
        return recipeService.getCount();
    }
    @GetMapping("/api/recipe-pageCount")
    public int getRecipePageCount() {
        return (int) Math.ceil ( (double)recipeService.getCount() / (double)RecipeService.RECIPES_PER_PAGE );
    }
    @GetMapping("/api/recipe-pageCount-'{name}'")
    public int getRecipePageCount(@PathVariable("name") String name) {
        return (int) Math.ceil ( (double)recipeService.getByNameCount(name) / (double)RecipeService.RECIPES_PER_PAGE );
    }

    @GetMapping("/api/recipe-pageSize")
    public int getRecipePageSize() {
        return RecipeService.RECIPES_PER_PAGE;
    }

    @GetMapping("/api/recipe-{page}")
    public Iterable<Recipe> findAll(@PathVariable("page") int page) {
        return recipeService.getAllRecipesPaged(new PageRequest(page, RecipeService.RECIPES_PER_PAGE));
    }

    @GetMapping("/api/recipe-search-'{name}'/{page}")
    public Iterable<Recipe> findByNamePaged(@PathVariable("name") String name, @PathVariable("page") int page) {
        return recipeService.getByNamePaged(name, new PageRequest(page, RecipeService.RECIPES_PER_PAGE));
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

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipeById(@PathVariable("id") String id) {
        Food2fork f2f = new Food2fork();
        return F2fDataConverter.convertRecipe(f2f.getRecipe("35120").getRecipee());
    }


    @PostMapping(value = "/api/recipe/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeAddResult saveRecipe(@RequestBody Recipe newRecipe) {
        RecipeAddResult recipeAddResult = new RecipeAddResult();
        if(newRecipe != null) {
            try {
                Recipe foundRecipe = recipeService.getRecipeByName(newRecipe.getName());
                if(foundRecipe != null && foundRecipe.getName().equals(newRecipe.getName())) {
                    recipeAddResult.success = false;
                    recipeAddResult.message = "There is already a recipe with given name!";
                }else{
                    recipeService.saveRecipe(newRecipe);
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
                Recipe foundRecipe = recipeService.getRecipeByName(newRecipeWithStringIngredients.getName());
                if(foundRecipe != null && foundRecipe.getName().equals(newRecipeWithStringIngredients.getName())) {
                    recipeAddResult.success = false;
                    recipeAddResult.message = "There is already a recipe with given name!";
                }else{
                    recipeService.saveRecipeWithStringIngredients(newRecipeWithStringIngredients);
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

    @DeleteMapping("api/recipe-delete/{id}")
    public void deleteRecipe(@PathVariable("id") long id) {
        recipeService.removeRecipe(id);
    }
}
