package com.awesomegroup.ingredients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wrobe on 25.04.2017.
 */

@Service
@Transactional
public class IngredientsService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public Iterable<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public boolean addIngredient(Ingredient ingredient) {
        boolean ingredientAdded = false;
        if(ingredient != null) {
            Optional<Ingredient> ingredientInBase = ingredientRepository.findIngredientsByName(ingredient.getIngredientName());
            if(!ingredientInBase.isPresent()) {
                ingredientRepository.save(ingredient);
                ingredientAdded = true;
            }
        }
        return ingredientAdded;
    }

    public Iterable<Boolean> addIngredient(Iterable<Ingredient> ingredients) {
        ArrayList<Boolean> results = new ArrayList<>();
        ingredients.forEach((Ingredient x) -> results.add(addIngredient(x)) );
        return results;
    }

    public Optional<Ingredient> findIngredientsByName(String name) {
        return ingredientRepository.findIngredientsByName(name);
    }

    public Integer deleteIngredientByName(String name) {
        return ingredientRepository.deleteIngredientByName(name);
    }
}
