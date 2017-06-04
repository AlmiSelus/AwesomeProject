package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Adi on 28.05.2017.
 */

@Repository
public interface FridgeRepository extends CrudRepository<Fridge, Long> {

    @Query("select f from Fridge f where f.fridgeId = :id")
    Fridge findById(@Param("id") long id);

    @Query ("select f.fridgeIngredients from Fridge f where f.fridgeId = :id")
    List<Ingredient> findIngredientsByFridge(@Param("id") long id);



}

//select * from fridge_ingredients where fridge_id =: .costam