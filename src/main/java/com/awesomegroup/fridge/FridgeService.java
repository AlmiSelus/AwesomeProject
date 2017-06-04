package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.user.User;
import com.awesomegroup.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-06-04.
 */
@Service
@Transactional
public class FridgeService {

    private final static Logger log = LoggerFactory.getLogger(FridgeService.class);

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    public boolean addFridgeIngredientsForUser(Principal principalUser, List<Ingredient> ingredients) {
        if(principalUser == null || ingredients.isEmpty()) {
            return false;
        }

        log.info("Principal user mail = {}", principalUser.getName());

        userRepository.findUserByEmail(principalUser.getName()).ifPresent(user -> {
            Fridge fridge = user.getFridge();
            fridge.getFridgeIngredients().clear();
            log.info("Fridge ingredients = {}", fridge.getFridgeIngredients().stream()
                    .map(Ingredient::getIngredientName)
                    .collect(Collectors.joining(", ")));
            ingredients.stream()
                    .map(ingredient -> ingredientsRepository.findByName(ingredient.getIngredientName()))
                    .forEach(optionalIngredient -> optionalIngredient.ifPresent(fridge.getFridgeIngredients()::add));
            log.info("Fridge ingredients AFTER = {}", fridge.getFridgeIngredients().stream()
                    .map(Ingredient::getIngredientName)
                    .collect(Collectors.joining(", ")));
            fridgeRepository.save(fridge);
        });

        return true;
    }

    public List<Ingredient> getCurrentIngredients(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .map(user -> user.getFridge().getFridgeIngredients())
                .orElse(Collections.emptyList());
    }
}