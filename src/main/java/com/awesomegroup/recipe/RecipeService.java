package com.awesomegroup.recipe;

import com.awesomegroup.food2fork.F2fDataConverter;
import com.awesomegroup.food2fork.Food2fork;
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
        return recipeRepository.findByDifficulty(recipeDifficulty.getID(), pageRequest == null ? new PageRequest(1, RECIPES_PER_PAGE) : pageRequest);
    }

    public int getCount() {
        return (int) recipeRepository.count() ;
    }

    public Recipe getRecipeByName(String name) {
        Food2fork f2f = new Food2fork();
        return F2fDataConverter.convertRecipe(f2f.getRecipe("35120").getRecipee());
        //return recipeRepository.findRecipeByName(name.replace("-", " "));
    }

    public void saveRecipe(Recipe recipe) {
        Optional.ofNullable(recipe).ifPresent(r -> recipeRepository.save(r));
    }
}
