package com.awesomegroup.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Michał on 2017-06-10.
 */
@JsonSerialize
@JsonDeserialize
public class FieldErrorJson {
    
    @JsonProperty
    private String field;
    
    @JsonProperty
    private String error;

    public static Builder create() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "FieldErrorJson{" +
                "field='" + field + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    public static class Builder {
        private FieldErrorJson fieldErrorJson;

        public Builder() {
            fieldErrorJson = new FieldErrorJson();
        }

        public Builder field(String field) {
            fieldErrorJson.field = field;
            return this;
        }

        public Builder message(String msg) {
            fieldErrorJson.error = msg;
            return this;
        }

        public FieldErrorJson build() {
            return fieldErrorJson;
        }
    }
}
