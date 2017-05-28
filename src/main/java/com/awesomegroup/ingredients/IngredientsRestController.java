package com.awesomegroup.ingredients;

import com.awesomegroup.food2fork.F2FRecipeRecipe;
import com.awesomegroup.food2fork.F2FSearchRecipe;

import com.awesomegroup.food2fork.F2FSearchResult;
import com.awesomegroup.food2fork.Food2fork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Ingredient OneIngredient() {
        return Ingredient.create()
                .id(5)
                .name("milk")
                .build();
    }

    @PostMapping("ingredients/add/{name}")
    public Boolean AddIngredient(@PathVariable("name") String name) {
        return ingredientService.AddIngredient(Ingredient.create().name(name).build());
    }

    @GetMapping("/ingredients/all")
    public List<Ingredient> GetAllIngredients() {
        List<Ingredient> result = new ArrayList<Ingredient>();
        ingredientService.getAllIngredients().forEach(x -> result.add(x));
        return result;
    }

    @GetMapping("/ingredients/find/{name}")
    public Ingredient FindByName(@PathVariable("name") String name) {
        Optional<Ingredient> foundIngredient = ingredientService.findIngredientsByName(name);
        return foundIngredient.isPresent() ? foundIngredient.get() : null;
    }

    @DeleteMapping("/ingredients/delete/{name}")
    public Integer deleteByName(@PathVariable("name") String name) {
        return ingredientService.deleteIngredientByName(name);
    }


    @GetMapping("/ingredients/getrecipe/{recipeId}")
    public F2FRecipeRecipe GetRecipe(@PathVariable("recipeId") String recipeId) {
        Food2fork food2fork = new Food2fork();
        F2FRecipeRecipe foundRecipe = food2fork.getRecipe(recipeId);
        return foundRecipe;
    }

    @GetMapping("/ingredients/kochastest/{param}")
    public String kochasTest(@PathVariable("param") String param) {
        String resultString = new String("Kochas is testing!");
        if(param != null) {
            resultString += "\n" + param;
        }
        return resultString;
    }
}
