package com.awesomegroup.user;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.Assert;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Michał on 2017-05-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class AuthServiceTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private AuthService authService;

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callLoadUserByUsername_userExists_shouldReturnCorrectUserDetails() {
        UserDetails userDetails = authService.loadUserByUsername("jsnow@westeros.com");
        Assert.notNull(userDetails, "User is null!");
        assertThat(userDetails, is(notNullValue()));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callLoadUserByUsername_userDoesNotExist_shouldThrowException() {
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("User with given email not found!");

        authService.loadUserByUsername("incorrectMail@mail.com");
    }

}
