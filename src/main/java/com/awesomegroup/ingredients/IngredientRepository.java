package com.awesomegroup.ingredients;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by wrobe on 25.04.2017.
 */

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    @Query("select i from Ingredient i where i.ingredientName = :name")
    Optional<Ingredient> findIngredientsByName(@Param("name") String ingredientName);

    @Modifying
    @Query("delete from Ingredient i where i.ingredientName = :name")
    Integer deleteIngredientByName(@Param("name") String ingredientName);

}
