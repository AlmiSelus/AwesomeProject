package com.awesomegroup.user;

import com.awesomegroup.general.FieldErrorJson;
import com.awesomegroup.general.ResponseEntityUtils;
import com.awesomegroup.general.ResponseJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-04-23.
 */
@RestController
public class UserRestController {

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/user")
    public Principal currentUser(@RequestBody AuthReq req) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static class AuthReq {
        String username;
        String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public static AuthReq create(String username, String password) {
            AuthReq requestData = new AuthReq();
            requestData.username = username;
            requestData.password = password;
            return requestData;
        }
    }

    @PostMapping("/api/user/check/mail")
    public DeferredResult<ResponseEntity<ResponseJson>> checkUserEmail(@RequestBody String mail) {
        DeferredResult<ResponseEntity<ResponseJson>> deferredCheck = new DeferredResult<>();
        userService.checkUserEmail(mail).subscribe(
                emailDoesExist -> deferredCheck.setResult(ResponseEntityUtils.ok(Boolean.toString(emailDoesExist))),
                error -> deferredCheck.setErrorResult(ResponseEntityUtils.notAcceptable(error.getMessage())));
        return deferredCheck;
    }

    @GetMapping("/api/user/login")
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/api/user/register")
    public DeferredResult<ResponseEntity<ResponseJson>> register(@Valid @RequestBody RegisterJson registerData, BindingResult result) throws JsonProcessingException {
        if(result.hasErrors()) {
            DeferredResult<ResponseEntity<ResponseJson>> defResult = new DeferredResult<>();
            List<FieldErrorJson> errorJsonList = mapValidationErrorMessages(result.getFieldErrors());
            defResult.setErrorResult(ResponseEntityUtils.notAcceptable(errorJsonList));
            return defResult;
        }

        log.info(registerData.toString());
        DeferredResult<ResponseEntity<ResponseJson>> deferredResult = new DeferredResult<>();
        userService.register(registerData).subscribe(user -> {
                    log.info("Test in here!");
                    deferredResult.setResult(ResponseEntity.ok(ResponseJson.create().message("/confirm").build()));
                },  error -> { deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error.getMessage()));
                    log.error(ExceptionUtils.getStackTrace(error));
        });
        return deferredResult;
    }

    private List<FieldErrorJson> mapValidationErrorMessages(List<FieldError> allErrors) {
        return allErrors.stream().map(objectError -> FieldErrorJson.create()
                .field(objectError.getField())
                .message(objectError.getDefaultMessage())
                .build()).map(fieldErrorJson -> {
                    log.info("FieldErrorJson = {}", fieldErrorJson.toString());
                    return fieldErrorJson;
        }).collect(Collectors.toList());
    }

    @PostMapping("/api/user/confirm")
    public ResponseEntity confirm(@RequestBody String userHash) {
        ResponseEntity status = userHash == null || userHash.isEmpty() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build() :
                ResponseEntity.status(HttpStatus.OK).build();

        if(status.getStatusCode() == HttpStatus.OK) {
            userService.confirm(userHash);
        }

        return status;
    }

}
