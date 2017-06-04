package com.awesomegroup.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Michał on 2017-05-28.
 */
public class ResponseEntityUtils {
    public static ResponseEntity<ResponseJson> ok(String message) {
        return ResponseEntity.ok(ResponseJson.create().message(message).build());
    }

    public static ResponseEntity<ResponseJson> notAcceptable(String message) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ResponseJson.create().message(message).build()
        );
    }
}
