package com.awesomegroup.ingredients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
