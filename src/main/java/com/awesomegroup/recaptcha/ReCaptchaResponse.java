package com.awesomegroup.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by c309044 on 2017-05-26.
 */
@JsonSerialize
public class ReCaptchaResponse {

    @JsonProperty("success")
    private boolean valid;

    @JsonProperty("challenge_ts")
    private String timestamp;

    private String hostname;

    @JsonProperty("error-codes")
    private List<String> errorCodes = new ArrayList<>();

    public boolean isValid() {
        return valid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    @Override
    public String toString() {
        return "ReCaptchaResponse{" +
                "valid=" + valid +
                ", timestamp='" + timestamp + '\'' +
                ", hostname='" + hostname + '\'' +
                ", errorCodes=" + errorCodes.stream().collect(Collectors.joining(", ")) +
                '}';
    }
}
