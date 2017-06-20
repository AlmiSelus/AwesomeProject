package com.awesomegroup.ingredients;

import com.awesomegroup.food2fork.F2FRecipeRecipe;
import com.awesomegroup.food2fork.Food2fork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by wrobe on 25.04.2017.
 */
@RestController
public class IngredientsRestController {

    private final IngredientsService ingredientService;

    @Autowired
    public IngredientsRestController(IngredientsService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients/example")
    public Ingredient oneIngredient() {
        return Ingredient.create()
                .id(5)
                .name("milk")
                .build();
    }

    @PostMapping("ingredients/add/{name}")
    public Boolean addIngredient(@PathVariable("name") String name) {
        return ingredientService.addIngredient(Ingredient.create().name(name).build());
    }

    @GetMapping("/api/ingredients/all")
    public List<Ingredient> getAllIngredients() {
        return ((List<Ingredient>) ingredientService.getAllIngredients()).stream()
                .map(ingredient -> Ingredient.create().name(ingredient.getIngredientName()).build())
                .collect(Collectors.toList());
    }

    @GetMapping("/ingredients/find/{name}")
    public Ingredient findByName(@PathVariable("name") String name) {
        Optional<Ingredient> foundIngredient = ingredientService.findIngredientsByName(name);
        return foundIngredient.isPresent() ? foundIngredient.get() : null;
    }

    @DeleteMapping("/ingredients/delete/{name}")
    public Integer deleteByName(@PathVariable("name") String name) {
        return ingredientService.deleteIngredientByName(name);
    }


    @GetMapping("/ingredients/getrecipe/{recipeId}")
    public F2FRecipeRecipe getRecipe(@PathVariable("recipeId") String recipeId) {
        Food2fork food2fork = new Food2fork();
        return food2fork.getRecipe(recipeId);
    }
}
