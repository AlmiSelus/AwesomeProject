package com.awesomegroup.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michał on 2017-04-23.
 */
@RestController
public class UserRestController {

    private final static Logger log = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/api/user/register")
    public ResponseEntity register(@RequestBody User user) {
        log.info(user.toString());
        return userService.register(user).map(u-> ResponseEntity.status(HttpStatus.OK))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)).build();
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
