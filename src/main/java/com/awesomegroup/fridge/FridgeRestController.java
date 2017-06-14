package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
    public void addIngredient(Principal principal, @RequestBody Ingredient picked) {
        log.info("\n\n\n Principal = {}", principal.toString());
        log.info("\n Ingredient = {} \n\n\n", picked.toString());
        fridgeService.addFridgeIngredientForUser(principal, picked);
    }

    @GetMapping("/api/fridge/ingredients")
    public List<Ingredient> getCurrentIngredients(Principal principal) {
        return fridgeService.getCurrentIngredients(principal).stream()
                .map(ingredient -> Ingredient.create().name(ingredient.getIngredientName()).build())
                .collect(Collectors.toList());
    }

}
