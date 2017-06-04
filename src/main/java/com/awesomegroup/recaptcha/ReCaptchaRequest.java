package com.awesomegroup.recaptcha;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Michał on 2017-05-25.
 */
@JsonSerialize
@JsonDeserialize
public class ReCaptchaRequest {
    private String secret;
    private String response;
    private String remoteip;

    public String getSecret() {
        return secret;
    }

    public String getResponse() {
        return response;
    }

    public String getRemoteip() {
        return remoteip;
    }

    public static Builder create() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ReCaptchaRequest{" +
                "secret='" + secret + '\'' +
                ", response='" + response + '\'' +
                ", remoteip='" + remoteip + '\'' +
                '}';
    }

    public static class Builder {
        private String secret;
        private String response;
        private String remoteip;

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder recaptchaResponse(String response) {
            this.response = response;
            return this;
        }

        public Builder remoteIP(String remoteip) {
            this.remoteip = remoteip;
            return this;
        }

        public ReCaptchaRequest build() {
            ReCaptchaRequest request = new ReCaptchaRequest();
            request.secret = secret;
            request.response = response;
            request.remoteip = remoteip;
            return request;
        }
    }
}
