package com.awesomegroup.user;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michał on 2017-05-17.
 */
public class UserBuilderTests {

    @Test
    public void userBuilder_createNotNullDefaultObject() {
        Assert.assertNotNull(User.create().build());
    }

    @Test
    public void userBuilder_createNotNullUserFromExisting() {
        User userBase = User.create().email("email").locked(true).build();
        Assert.assertNotNull(User.create(userBase).build());
    }

    @Test
    public void userBuilder_createObjectString() {
        String userString = "User{id=0, name='null', surname='null', email='test@test.com', enabled=true, credentialsExpired=true, locked=true, userRoles=[]}";
        User user = User.create().email("test@test.com")
                .enabled(true)
                .locked(true)
                .credentialsExpired(true)
                .build();
        Assert.assertEquals(userString, user.toString());
    }

    @Test
    public void userBuilder_createUserWithPassword() {
        User user = User.create().password("password1").build();
        Assert.assertEquals("password1", user.getPassword());
    }

    @Test
    public void userBuilder_copyUserDataFromExisting() {
        User user1 = User.create().email("email").password("password").build();
        User user2 = User.create(user1).build();

        Assert.assertEquals(user1.toString(), user2.toString());
    }

}
