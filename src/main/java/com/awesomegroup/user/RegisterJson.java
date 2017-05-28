package com.awesomegroup.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by c309044 on 2017-05-26.
 */
@JsonSerialize
@JsonDeserialize
public class RegisterJson {
    private String email;
    private String password;
    private String name;
    private String surname;
    @JsonProperty("g-recaptcha-response")
    private String gResponse;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCaptchaResponse() {
        return gResponse;
    }
}
