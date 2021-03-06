package com.awesomegroup.ingredients;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by wrobe on 25.04.2017.
 */

@Repository
public interface IngredientsRepository extends CrudRepository<Ingredient, Long> {
    @Query("select i from Ingredient i where i.ingredientName = :name")
    Optional<Ingredient> findByName(@Param("name") String ingredientName);
}
