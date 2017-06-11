package com.awesomegroup.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by c309044 on 2017-05-26.
 */
@JsonSerialize
@JsonDeserialize
public class RegisterJson {

    @NotNull(message = "Email not provided")
    @Email
    @Size(min = 5, message = "Email too short")
    private String email;

    @NotNull(message = "Password not provided")
    @Size(min = 8, message = "Password is too short")
    private String password;

    @NotNull(message = "Name not provided")
    @Size(min = 1, message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Surname not provided")
    @Size(min = 1, message = "Surname cannot be empty")
    private String surname;

    @NotNull
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

    @Override
    public String toString() {
        return "RegisterJson{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gResponse='" + gResponse + '\'' +
                '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private RegisterJson registerJson;
        public Builder() {
            registerJson = new RegisterJson();
        }

        public Builder email(String email) {
            registerJson.email = email;
            return this;
        }

        public Builder name(String name) {
            registerJson.name = name;
            return this;
        }

        public Builder password(String password) {
            registerJson.password = password;
            return this;
        }

        public Builder surname(String surname) {
            registerJson.surname = surname;
            return this;
        }

        public Builder googleResponse(String response) {
            registerJson.gResponse = response;
            return this;
        }

        public RegisterJson build() {
            return registerJson;
        }
    }
}
