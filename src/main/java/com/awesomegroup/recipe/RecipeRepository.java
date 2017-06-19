package com.awesomegroup.recipe;

import org.springframework.data.domain.Page;
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
    Page<Recipe> findByDifficulty(@Param("difficulty") byte recipeDifficulty, Pageable pageRequest);

//    @Query("select r from Recipe r where r.name = :name")
    @Query("select r from Recipe r where r.name like %:name%")
    Page<Recipe> findByNamePaged(@Param("name") String name, Pageable pageRequest);

    @Query("select count(*) from Recipe r where r.name like %:name%")
    long findByNameCount(@Param("name") String name);

    @Query("select r from Recipe r")
    Page<Recipe> findAllPaged(Pageable pageRequest);

    @Query("select r from Recipe r where r.name = :name")
    Recipe findRecipeByName(@Param("name") String name);
}
