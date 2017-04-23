package com.awesomegroup.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Michał on 2017-04-23.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>{

    @Query("select u from User u where u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

}
