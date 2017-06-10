package com.awesomegroup.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private ReCaptchaResponse reCaptchaResponse;

        public Builder() {
            reCaptchaResponse = new ReCaptchaResponse();
        }

        public Builder isValid(boolean valid) {
            reCaptchaResponse.valid = valid;
            return this;
        }

        public Builder timestamp(String timestamp) {
            reCaptchaResponse.timestamp = timestamp;
            return this;
        }

        public Builder hostname(String hostname) {
            reCaptchaResponse.hostname = hostname;
            return this;
        }

        public Builder errorCodes(String... errorCode) {
            reCaptchaResponse.errorCodes.clear();
            reCaptchaResponse.errorCodes.addAll(Arrays.asList(errorCode));
            return this;
        }

        public ReCaptchaResponse build() {
            return reCaptchaResponse;
        }
    }
}
