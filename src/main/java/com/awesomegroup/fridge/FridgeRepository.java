package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipe.Recipe;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Michał on 2017-06-04.
 */
@Repository
public interface FridgeRepository extends CrudRepository<Fridge, Long> {

    @Modifying
    @Query("delete from FridgeIngredient fi where fi.ingredient = :ingredient and fi.fridge = :fridge")
    void deleteFridgeIngredient(@Param("fridge") Fridge fridge, @Param("ingredient") Ingredient ingredient);

    @Query("select distinct r from Recipe r join r.recipeIngredients ri where ri.ingredient.ingredientName in :names")
    List<Recipe> findAllRecipesByIngredients(@Param("names") List<String> ingredientNames);

}
