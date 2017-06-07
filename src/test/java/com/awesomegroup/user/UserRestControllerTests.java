package com.awesomegroup.user;

import com.awesomegroup.WebSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Michał on 2017-05-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class, WithSecurityContextTestExecutionListener.class})
@Import(WebSecurityConfig.class)
public class UserRestControllerTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCurrentUser_shouldObtainRegisteredUser() throws Exception {
        UserRestController.AuthReq authReq = new UserRestController.AuthReq();
        authReq.username = "jsnow@westeros.com";
        authReq.password = "test";
        mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(authReq)))
                    .andExpect(status().isOk())
                    .andExpect(authenticated())
                    .andExpect(jsonPath("$.name", is("jsnow@westeros.com")))
                    .andExpect(jsonPath("$.authenticated", is(true)));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCurrentUser_incorrectCredentials_shouldReturn403WithMessage() throws Exception {
        UserRestController.AuthReq authReq = new UserRestController.AuthReq();
        authReq.username = "jsnow@westeros.com";
        authReq.password = "incorrectPassword";
        mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated())
                .andExpect(status().reason("Bad credentials"));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCurrentUser_userLocked_shouldReturn403WithMessage() throws Exception {
        UserRestController.AuthReq authReq = new UserRestController.AuthReq();
        authReq.username = "sbean@westeros.com";
        authReq.password = "test";
        mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated())
                .andExpect(status().reason("User account is locked"));

    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCurrentUser_userDisabled_shouldReturn403WithMessage() throws Exception {
        UserRestController.AuthReq authReq = new UserRestController.AuthReq();
        authReq.username = "rstark@westeros.com";
        authReq.password = "test";
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated())
                .andExpect(status().reason("User is disabled"));

    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCurrentUser_userCredentialsExpired_shouldReturn403WithMessage() throws Exception {
        UserRestController.AuthReq authReq = new UserRestController.AuthReq();
        authReq.username = "jrrm@westeros.com";
        authReq.password = "test";
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated())
                .andExpect(status().reason("User credentials have expired"));

    }

}
