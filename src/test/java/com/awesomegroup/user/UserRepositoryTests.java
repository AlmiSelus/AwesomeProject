package com.awesomegroup.user;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Michał on 2017-05-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callFindByEmail_emailInDB_shouldReturnExistingUser() {
        Optional<User> user = userRepository.findUserByEmail("jsnow@westeros.com");
        assertThat(user.isPresent(), is(true));
        user.ifPresent(Assert::assertNotNull);
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callFindByEmail_emailNOTInDB_shouldReturnEmptyOptional() {
        Optional<User> user = userRepository.findUserByEmail("test");
        assertThat(user.isPresent(), is(false));
    }
}
