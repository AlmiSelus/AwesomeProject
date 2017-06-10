package com.awesomegroup.general;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.exceptions.Exceptions;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Michał on 2017-05-28.
 */
public class ResponseEntityUtils {

    private ResponseEntityUtils() {

    }

    public static ResponseEntity<ResponseJson> ok(String message) {
        return ResponseEntity.ok(ResponseJson.create().message(message).build());
    }

    public static ResponseEntity<ResponseJson> notAcceptable(Object message) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                ResponseJson.create().message(message).build()
        );
    }

}
