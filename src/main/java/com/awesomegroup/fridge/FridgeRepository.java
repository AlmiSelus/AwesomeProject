package com.awesomegroup.fridge;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Michał on 2017-06-04.
 */
@Repository
public interface FridgeRepository extends CrudRepository<Fridge, Long> {

}
