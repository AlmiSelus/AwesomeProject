package com.awesomegroup.user;

import io.reactivex.Observable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.security.Principal;

/**
 * Created by Michał on 2017-04-23.
 */
@RestController
public class UserRestController {

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public Principal currentUser(Principal user) {
        return user;
    }

    @PostMapping("/api/user/login")
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/api/user/register")
    public DeferredResult<ResponseEntity<String>> register(@RequestBody RegisterJson registerData) {
        log.info(registerData.toString());
        Observable<User> observableUser = userService.register(registerData);
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
        observableUser.subscribe(user -> deferredResult.setResult(ResponseEntity.ok("")),
                                 error -> {
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error.getMessage()));
            log.error(ExceptionUtils.getStackTrace(error));
        });
        return deferredResult;
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
