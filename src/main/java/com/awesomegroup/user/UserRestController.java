package com.awesomegroup.user;

import com.awesomegroup.general.ResponseEntityUtils;
import com.awesomegroup.general.ResponseJson;
import io.reactivex.Maybe;
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

    @PostMapping("/api/user/check/mail")
    public DeferredResult<ResponseEntity<ResponseJson>> checkUserEmail(@RequestBody String mail) {
        DeferredResult<ResponseEntity<ResponseJson>> deferredCheck = new DeferredResult<>();
        userService.checkUserEmail(mail).subscribe(
                emailDoesExist -> deferredCheck.setResult(ResponseEntityUtils.ok(Boolean.toString(emailDoesExist))),
                error -> deferredCheck.setErrorResult(ResponseEntityUtils.notAcceptable(error.getMessage())));
        return deferredCheck;
    }

    @PostMapping("/api/user/login")
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/api/user/register")
    public DeferredResult<ResponseEntity<ResponseJson>> register(@RequestBody RegisterJson registerData) {
        log.info(registerData.toString());
        DeferredResult<ResponseEntity<ResponseJson>> deferredResult = new DeferredResult<>();
        userService.register(registerData).subscribe(user -> deferredResult.setResult(ResponseEntity.ok(ResponseJson.create().message("/confirm").build())),
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
