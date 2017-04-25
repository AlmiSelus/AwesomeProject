package com.awesomegroup.ingredients;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wrobe on 25.04.2017.
 */

@Repository
public interface IngredientsRepository extends CrudRepository<Ingredient, Long> {
}
