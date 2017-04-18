package com.awesomegroup.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Michał on 2017-04-14.
 */
@Service
@Transactional //for future use with inserting data to db
public class RecipeService {

    public static final int RECIPES_PER_PAGE = 10;

    @Autowired
    private RecipeRepository recipeRepository;

    public Iterable<Recipe> getAllRecipesPaged(PageRequest pageRequest) {
        return recipeRepository.findAllPaged(pageRequest);
    }

    public Iterable<Recipe> getByDifficulty(RecipeDifficulty recipeDifficulty, PageRequest pageRequest) {
        pageRequest = pageRequest == null ? new PageRequest(1, RECIPES_PER_PAGE) : pageRequest;
        return recipeRepository.findByDifficulty(recipeDifficulty.getID(), pageRequest);
    }

    public int getCount() {
        return (int) recipeRepository.count() ;
    }

    public Recipe getRecipeByName(String name) {
        return recipeRepository.findRecipeByName(name.replace("-", " "));
    }

    public void saveRecipe(Recipe recipe) {
        Optional.ofNullable(recipe).ifPresent(r -> recipeRepository.save(r));
    }
}
