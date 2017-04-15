package com.awesomegroup.recipe;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Michał on 2017-04-14.
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query("select r from Recipe r where r.difficulty = :difficulty")
    Iterable<Recipe> findByDifficulty(@Param("difficulty") byte recipeDifficulty, Pageable pageRequest);

    @Query("select r from Recipe r")
    Iterable<Recipe> findAllPaged(Pageable pageRequest);

    @Query("select r from Recipe r where r.name = :name")
    Recipe findRecipeByName(@Param("name") String name);
}
