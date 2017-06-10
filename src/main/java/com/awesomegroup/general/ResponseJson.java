package com.awesomegroup.general;

/**
 * Created by Michał on 2017-05-28.
 */
public class ResponseJson {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private ResponseJson responseJson;

        public Builder() {
            responseJson = new ResponseJson();
        }

        public Builder message(Object message) {
            responseJson.message = message;
            return this;
        }

        public ResponseJson build() {
            return responseJson;
        }

    }
}
