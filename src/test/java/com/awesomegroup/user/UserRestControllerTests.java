package com.awesomegroup.user;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Michał on 2017-05-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class UserRestControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserRestControllerTests())
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCurrentUser_shouldObtainRegisteredUser() throws Exception {
//        System.out.println("Password: " + passwordEncoder.encode("password"));
//        mockMvc.perform(post("/api/user/login")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("username", "jsnow@westeros.com")
//                .param("password", "password")
//        )
//                .andExpect(status().isOk());
    }

}