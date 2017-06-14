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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;

import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("jsnow@westeros.com", "test");
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
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("jsnow@westeros.com", "incorrectPassword");
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
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("sbean@westeros.com", "test");
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
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("rstark@westeros.com", "test");
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
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("jrrm@westeros.com", "test");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated())
                .andExpect(status().reason("User credentials have expired"));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldRegisterUser() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("newMail@mail.com")
                .name("name1")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        mockMvc.perform(post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());
        assertThat(userRepository.findUserByEmail("newMail@mail.com").isPresent(), is(true));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_incorrectEmail() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("incorrectMail")
                .name("name1")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("email")))
                .andExpect(jsonPath("$.message[0].error", is("not a well-formed email address")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_userWithEmailExists() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("grrm@westeros.com")
                .name("name1")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message", is("User with given email already exists")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_nullMail() throws Exception {
        RegisterJson register = RegisterJson.create()
                .name("name1")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("email")))
                .andExpect(jsonPath("$.message[0].error", is("Email not provided")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_emptyMail() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("")
                .name("name1")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("email")))
                .andExpect(jsonPath("$.message[0].error", is("Email too short")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_emptyName() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("valid@email.com")
                .name("")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("name")))
                .andExpect(jsonPath("$.message[0].error", is("Name cannot be empty")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_nullName() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("valid@email.com")
                .surname("surname1")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("name")))
                .andExpect(jsonPath("$.message[0].error", is("Name not provided")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_emptySurname() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("valid@email.com")
                .name("testname")
                .surname("")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("surname")))
                .andExpect(jsonPath("$.message[0].error", is("Surname cannot be empty")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_nullSurname() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("valid@email.com")
                .name("testname")
                .password("superSecretPassword")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("surname")))
                .andExpect(jsonPath("$.message[0].error", is("Surname not provided")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_nullPassword() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("valid@email.com")
                .name("testname")
                .surname("testsurname")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("password")))
                .andExpect(jsonPath("$.message[0].error", is("Password not provided")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callRegister_shouldNotRegisterUser_shortPassword() throws Exception {
        RegisterJson register = RegisterJson.create()
                .email("valid@email.com")
                .name("testname")
                .surname("testsurname")
                .password("short")
                .googleResponse("validSecret")
                .build();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(register)))
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message.length()", is(1)))
                .andExpect(jsonPath("$.message[0].field", is("password")))
                .andExpect(jsonPath("$.message[0].error", is("Password is too short")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callLogout_shouldLogoutCorrectly() throws Exception {
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("jsnow@westeros.com", "test");
        HttpSession authenticatedSession = mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(authReq)))
                .andDo(print())
                .andExpect(authenticated())
                .andReturn()
                .getRequest()
                .getSession();

        mockMvc.perform(post("/api/user/logout")
                    .session((MockHttpSession) authenticatedSession))
                .andExpect(status().isOk())
                .andExpect(unauthenticated())
                .andExpect(jsonPath("$.message", is("/login")));
    }

    @Test
    public void createAuthReq_shouldCreateCorrectlyStoredAuthReqData() {
        UserRestController.AuthReq authReq = UserRestController.AuthReq.create("jsnow@westeros.com", "test");
        assertThat(authReq.getUsername(), is("jsnow@westeros.com"));
        assertThat(authReq.getPassword(), is("test"));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCheckUserEmail_emailDoesNotExist_shouldReturnMessageFalse() throws Exception {
        String testedMail = "test@test.ok.com";
        MvcResult result = mockMvc.perform(post("/api/user/check/mail").content(testedMail)).andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("false")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callCheckUserEmail_emailExists_shouldReturnErrorWithMessage() throws Exception {
        String testedMail = "jsnow@westeros.com";
        MvcResult result = mockMvc.perform(post("/api/user/check/mail").content(testedMail)).andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("User with given e-mail already exists.")));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callConfirm_shouldConfirmUser() throws Exception {
        Optional<User> user = userRepository.findUserByEmail("grrm@westeros.com");
        User u = user.map(Function.identity()).orElse(null);

        assertThat(u, is(notNullValue()));
        assertThat(u.isEnabled(), is(false));
        assertThat(u.isLocked(), is(true));

        String userHash = Base64Utils.encodeToString((u.getEmail() + "2hM$^%#$^64Jpx5*NG#^E6yaRXLq6PhgmC&Yx61rzKgCPJdpZWx(ipq%fk)&HFjz").getBytes());
        mockMvc.perform(post("/api/user/confirm").content(userHash))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("/confirm")));

        Optional<User> confirmedOptional = userRepository.findUserByEmail("grrm@westeros.com");
        User confirmed = confirmedOptional.map(Function.identity()).orElse(null);
        assertThat(confirmed, is(notNullValue()));
        assertThat(confirmed.isEnabled(), is(true));
        assertThat(confirmed.isLocked(), is(false));
    }

    @Test
    @DatabaseSetup("/database/user2Entries.xml")
    public void callConfirm_userHashIsEmpty_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/user/confirm").content(""))
                .andExpect(status().isBadRequest());
    }

}
