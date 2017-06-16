package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Michał on 2017-06-04.
 */
@Repository
public interface FridgeRepository extends CrudRepository<Fridge, Long> {

    @Modifying
    @Query("delete from FridgeIngredient fi where fi.ingredient = :ingredient and fi.fridge = :fridge")
    void deleteFridgeIngredient(@Param("fridge") Fridge fridge, @Param("ingredient") Ingredient ingredient);

}
