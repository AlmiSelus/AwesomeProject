package com.awesomegroup.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by wrobe on 10.06.2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeAddResult {

    @JsonProperty("success")
    public Boolean success;
    @JsonProperty("message")
    public String message;

    RecipeAddResult() {
        success = true;
        message = "";
    }
    RecipeAddResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private RecipeAddResult recipeAddResult = new RecipeAddResult();

        public Builder success(Boolean success) {
            this.recipeAddResult.success = success;
            return this;
        }
        public Builder message(String message) {
            this.recipeAddResult.message = message;
            return this;
        }
        public RecipeAddResult build(){
            return recipeAddResult;
        }
    }
}
