package com.awesomegroup.user;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michał on 2017-05-17.
 */
public class UserBuilderTests {

    @Test
    public void userBuilderTest() {
        String email = "test@test.com";
        User user = User.create().email(email).build();
        Assert.assertEquals(email, user.getEmail());
    }
}
