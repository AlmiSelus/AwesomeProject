package com.awesomegroup.user;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Michał on 2017-05-17.
 */
public class UserBuilderTests {

    @Test
    public void userBuilder_createNotNullDefaultObject() {
        assertThat(User.create().build(), is(notNullValue()));
    }

    @Test
    public void userBuilder_createNotNullUserFromExisting() {
        User userBase = User.create().email("email").locked(true).build();
        assertThat(User.create(userBase).build(), is(notNullValue()));
    }

    @Test
    public void userBuilder_createObjectString() {
        String userString = "User{id=0, name='null', surname='null', email='test@test.com', enabled=true, credentialsExpired=true, locked=true, userRoles=[]}";
        User user = User.create().email("test@test.com")
                .enabled(true)
                .locked(true)
                .credentialsExpired(true)
                .build();
        assertThat(user.getId(), is(0L));
        assertThat(user.toString(), is(userString));
    }

    @Test
    public void userBuilder_createUserWithPassword() {
        User user = User.create().password("password1").build();
        assertThat(user.getPassword(), is("password1"));
    }

    @Test
    public void userBuilder_copyUserDataFromExisting() {
        User user1 = User.create().email("email").password("password").build();
        User user2 = User.create(user1).build();
        assertThat(user2.toString(), is(user1.toString()));
    }

}
