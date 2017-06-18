package com.awesomegroup.fridge;

import com.awesomegroup.fridge.ingredient.FridgeIngredientJson;
import com.awesomegroup.general.ResponseEntityUtils;
import com.awesomegroup.general.ResponseJson;
import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipe.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michał on 2017-06-04.
 */
@RestController
public class FridgeRestController {

    private static final Logger log = LoggerFactory.getLogger(FridgeRestController.class);

    @Autowired
    private FridgeService fridgeService;

    @PostMapping("/api/fridge/addIngredients")
    public void addIngredients(Principal principal, @RequestBody List<Ingredient> selectedIngredients) {
        log.info("\n Principal = {}", principal.toString());
        fridgeService.addFridgeIngredientsForUser(principal, selectedIngredients);
    }

    @PostMapping("/api/fridge/addIngredient")
    public void addIngredient(Principal principal, @RequestBody FridgeIngredientJson picked) {
        log.info("\n\n\n Principal = {}", principal.toString());
        log.info("\n Ingredient = {} \n\n\n", picked.toString());
        fridgeService.addFridgeIngredientForUser(principal, picked);
    }

    @GetMapping("/api/fridge/ingredients")
    public List<FridgeIngredientJson> getCurrentIngredients(Principal principal) {
        return fridgeService.getCurrentIngredients(principal);
    }

    @PostMapping("/api/fridge/recipes/matching")
    public List<Recipe> findAllMatchingRecipes(Principal principal, @RequestBody List<String> ingredients) {
        return fridgeService.findAllFittingRecipes(ingredients);
    }

    @DeleteMapping("/api/fridge/ingredient/remove/{name}")
    public ResponseEntity<ResponseJson> removeIngredient(Principal principal, @PathVariable("name") String ingredientName) {
        return Optional.of(fridgeService.removeFridgeIngredientForUser(principal, ingredientName))
                .map(b->ResponseEntityUtils.ok(""))
                .orElse(ResponseEntityUtils.notAcceptable("Could not remove"));
    }

}
