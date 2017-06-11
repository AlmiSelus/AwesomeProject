package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-06-04.
 */
@Service
@Transactional
public class FridgeService {

    private static final Logger log = LoggerFactory.getLogger(FridgeService.class);

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    public boolean addFridgeIngredientsForUser(Principal principalUser, List<Ingredient> ingredients) {
        if(principalUser == null || ingredients == null || ingredients.isEmpty()) {
            return false;
        }

        log.info("Principal user mail = {}", principalUser.getName());

        return userRepository.findUserByEmail(principalUser.getName()).map(user->{
            Fridge fridge = user.getFridge();
            fridge.getFridgeIngredients().clear();
            ingredients.stream()
                    .map(ingredient -> ingredientsRepository.findByName(ingredient.getIngredientName()))
                    .forEach(optionalIngredient -> optionalIngredient.ifPresent(fridge.getFridgeIngredients()::add));
            fridgeRepository.save(fridge);
            return true;
        }).orElse(false);
    }

    public List<Ingredient> getCurrentIngredients(Principal principal) {
        return userRepository.findUserByEmail(principal.getName())
                .map(user -> user.getFridge().getFridgeIngredients())
                .orElse(Collections.emptyList());
    }
}
