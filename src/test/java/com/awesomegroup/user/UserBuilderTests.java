package com.awesomegroup.user;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

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

    @Test
    public void userBuilder_createUserWithRoleADMIN() {
        User user = User.create().email("some.mail@mail.com").roles(UserRole.ADMIN_ROLE).build();
        assertThat(user.getUserRoles(), hasSize(1));
        assertThat(user.getUserRoles().get(0).getRole(), is(UserRole.ADMIN_ROLE.getRole()));
    }

    @Test
    public void userBuilder_createUserWithEmptyRoles() {
        User user = User.create().build();
        assertThat(user.getUserRoles(), is(notNullValue()));
        assertThat(user.getUserRoles(), hasSize(0));
    }

}
