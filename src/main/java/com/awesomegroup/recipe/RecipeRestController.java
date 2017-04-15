package com.awesomegroup.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/api/recipe-{page}")
    public Iterable<Recipe> findAll(@PathVariable("page") int page) {
        return recipeService.getAllRecipesPaged(createPageRequest(page, recipeService.getMaxPage()));
    }

    @GetMapping("/api/recipe/difficulty-{diff}/{page}")
    public Iterable<Recipe> findAllByDifficulty(@PathVariable("diff") String difficulty, @PathVariable("page") int page) {
        RecipeDifficulty recipeDifficulty = RecipeDifficulty.findByName(difficulty);
        return recipeService.getByDifficulty(recipeDifficulty, createPageRequest(page, recipeService.getMaxPage()));
    }

    @GetMapping("/api/recipe/{name}")
    public Recipe getRecipeByName(@PathVariable("name") String name) {
        return recipeService.getRecipeByName(name);
    }

    @PostMapping("/api/recipe/add")
    @ResponseStatus(value = HttpStatus.OK)
    public void addRecipe(@RequestBody Recipe recipe) {
        recipeService.saveRecipe(recipe);
    }

    private PageRequest createPageRequest(int page, int maxPages) {
        page = page < 1 ? 1 : page;
        page = page > maxPages ? maxPages : page;
        return new PageRequest(page, maxPages);
    }
}
